package org.open.gateway.route.security;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.open.gateway.route.utils.UrlUtil;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

/**
 * Created by miko on 2020/7/1.
 * 资源授权管理器
 *
 * @author MIKO
 */
@Slf4j
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    /**
     * 直接放行的权限
     */
    private static final Set<String> PERMIT_ROLES = CollectionUtil.newHashSet("admin");
    /**
     * 资源服务
     */
    private final RefreshableRouteDefinitionRepository resourceRepository;
    /**
     * 客户端资源服务
     */
    private final RefreshableClientResourcesRepository clientResourcesRepository;
    /**
     * ip限制资源服务
     */
    private final RefreshableIpLimitRepository ipLimitRepository;

    public AuthorizationManager(RefreshableRouteDefinitionRepository resourceRepository,
                                RefreshableClientResourcesRepository clientResourcesRepository,
                                RefreshableIpLimitRepository ipLimitRepository) {
        this.resourceRepository = resourceRepository;
        this.clientResourcesRepository = clientResourcesRepository;
        this.ipLimitRepository = ipLimitRepository;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid token"))))
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof TokenUser)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid principal"))))
                .cast(TokenUser.class)
                .flatMap(user -> checkClientResourceAuthorities(authorizationContext.getExchange(), user))
                .map(AuthorizationDecision::new);
    }

    /**
     * 是否直接通过
     *
     * @param user 用户信息
     * @return 是否通过
     */
    private boolean isPermit(TokenUser user) {
        Collection<String> authorities = user.getAuthorities();
        return authorities != null && authorities.stream().anyMatch(PERMIT_ROLES::contains);
    }

    /**
     * 校验客户端资源权限
     *
     * @param exchange web路由交换信息
     * @param user     请求的用户信息
     * @return 验证结果
     */
    private Mono<Boolean> checkClientResourceAuthorities(ServerWebExchange exchange, TokenUser user) {
        String requestPath = WebExchangeUtil.getRequestPath(exchange);
        String ipAddress = WebExchangeUtil.getRemoteAddress(exchange);
        String clientId = user.getClientId();
        Collection<String> authorities = user.getAuthorities();
        log.info("Request path:{} ip:{} client_id:{} authorities:{}", requestPath, ipAddress, clientId, authorities);
        // 存放客户端id
        WebExchangeUtil.putClientId(exchange, clientId);
        // 是否直接放行
        if (this.isPermit(user)) {
            return Mono.just(true);
        }
        // 清理请求路径
        String path = UrlUtil.trimUrlParameter(requestPath);
        // 获取接口资源
        Mono<RouteDefinition> routeDefinition = resourceRepository.loadRouteDefinition(path);
        return routeDefinition
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid path:" + path))))
                .flatMap(r -> Flux.merge(
                        // 匹配黑白名单
                        matchIpLimit(r, ipAddress),
                        // 验证是否拥有资源权限
                        matchResource(r, clientId)
                        ).all(b -> b)
                );
    }

    /**
     * 匹配黑白名单
     *
     * @param routeDefinition 路由信息
     * @param ip              ip地址
     * @return 匹配允许访问 true允许，false不允许
     */
    private Mono<Boolean> matchIpLimit(RouteDefinition routeDefinition, String ip) {
        // 获取api编码
        String apiCode = RouteDefinitionUtil.getApiCode(routeDefinition);
        return ipLimitRepository.loadIpLimitByApi(apiCode)
                .map(ipLimits -> ipLimits.isAccessAllowed(ip))
                .switchIfEmpty(Mono.defer(() -> Mono.just(true))); // 没有黑白名单默认为true, 允许访问
    }

    /**
     * 匹配是否拥有资源
     *
     * @param routeDefinition 路由配置
     * @param clientId        客户端id
     * @return 匹配结果
     */
    private Mono<Boolean> matchResource(RouteDefinition routeDefinition, String clientId) {
        if (!RouteDefinitionUtil.getIsAuth(routeDefinition)) {
            log.info("No need authorization api:{}", routeDefinition.getId());
            return Mono.just(true);
        }
        return this.clientResourcesRepository
                .loadResourcesByClientId(clientId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid client_id:" + clientId))))
                .map(resources -> resources.contains(routeDefinition.getId()));
    }

}
