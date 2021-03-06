package org.open.gateway.route.security;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.common.utils.CollectionUtil;
import org.open.gateway.common.utils.UrlUtil;
import org.open.gateway.route.entity.token.TokenUser;
import org.open.gateway.route.exception.RouteNotFoundException;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.open.gateway.route.utils.WebExchangeUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
     * 资源服务
     */
    private final RefreshableRouteDefinitionRepository resourceRepository;
    /**
     * ip限制资源服务
     */
    private final RefreshableIpLimitRepository ipLimitRepository;

    public AuthorizationManager(RefreshableRouteDefinitionRepository resourceRepository,
                                RefreshableIpLimitRepository ipLimitRepository) {
        this.resourceRepository = resourceRepository;
        this.ipLimitRepository = ipLimitRepository;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .cast(TokenUser.class)
                .flatMap(user -> checkClientResourceAuthorities(authorizationContext.getExchange(), user))
                .map(AuthorizationDecision::new);
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
        log.info("Request path:{} ip:{} client_id:{} authorities:{}", requestPath, ipAddress, clientId, user.getAuthorities());
        // 存放客户端id
        WebExchangeUtil.putClientId(exchange, clientId);
        // 清理请求路径
        String path = UrlUtil.trimUrlParameter(requestPath);
        // 获取接口资源
        Mono<RouteDefinition> routeDefinition = resourceRepository.loadRouteDefinition(path);
        return routeDefinition
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RouteNotFoundException("Request path not found: " + path))))
                .flatMap(r -> Flux.merge(
                        // 匹配黑白名单
                        matchIpLimit(r, ipAddress),
                        // 验证是否拥有资源权限
                        matchResource(r, user)
                ).all(b -> b));
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
     * @param user            客户端信息
     * @return 匹配结果
     */
    private Mono<Boolean> matchResource(RouteDefinition routeDefinition, TokenUser user) {
        if (!RouteDefinitionUtil.getIsAuth(routeDefinition)) {
            log.info("No need authorization api:{}", routeDefinition.getId());
            return Mono.just(true);
        }
        // 获取api编码
        String apiCode = RouteDefinitionUtil.getApiCode(routeDefinition);
        // 分组代码
        Set<String> groupCode = RouteDefinitionUtil.getGroupCodes(routeDefinition);
        return Mono.just(user.getAuthorities().contains(apiCode) || CollectionUtil.containsAny(groupCode, user.getScopes()));
    }

}
