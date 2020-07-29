package org.open.gateway.route.security;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.entity.token.TokenUser;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
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

    // 直接放行的权限
    private static final Set<String> PERMIT_ROLES = CollectionUtil.newHashSet("admin");
    // 资源服务
    private final RefreshableRouteDefinitionRepository resourceRepository;
    // 客户端资源服务
    private final RefreshableClientResourcesRepository clientResourcesRepository;

    public AuthorizationManager(RefreshableRouteDefinitionRepository resourceRepository, RefreshableClientResourcesRepository clientResourcesRepository) {
        this.resourceRepository = resourceRepository;
        this.clientResourcesRepository = clientResourcesRepository;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        // 获取请求路径
        String requestPath = getRequestPath(authorizationContext);
        return authentication
                .filter(auth -> auth.isAuthenticated() && auth.getPrincipal() != null)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid token"))))
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof TokenUser)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid principal"))))
                .cast(TokenUser.class)
                .flatMap(user -> checkClientResourceAuthorities(requestPath, user))
                .map(AuthorizationDecision::new);
    }

    /**
     * 获取请求路径
     *
     * @param authorizationContext 认证信息
     * @return 请求路径
     */
    private String getRequestPath(AuthorizationContext authorizationContext) {
        String requestPath = authorizationContext.getExchange().getRequest().getURI().getPath();
        log.info("Request path:{}", requestPath);
        return requestPath;
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
     * 清理请求url参数
     *
     * @param requestPath 原始请求路径
     * @return 清理后的请求路径
     */
    private String trimRequestPath(String requestPath) {
        int index = requestPath.indexOf("?");
        if (index == -1) {
            return requestPath;
        }
        return requestPath.substring(0, index);
    }

    /**
     * 校验客户端资源权限
     *
     * @param requestPath 请求资源路径
     * @param user        请求的用户信息
     * @return 验证结果
     */
    private Mono<Boolean> checkClientResourceAuthorities(String requestPath, TokenUser user) {
        // 是否直接放行
        if (this.isPermit(user)) {
            return Mono.just(true);
        }
        String clientId = user.getClientId();
        log.debug("Request token client id:{}", clientId);
        Collection<String> authorities = user.getAuthorities();
        log.debug("Request token authorities:{}", authorities);
        // 清理请求路径
        String path = trimRequestPath(requestPath);
        // 获取接口资源
        Mono<RouteDefinition> routeDefinition = resourceRepository.getRouteDefinition(path);
        return routeDefinition
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid path:" + path))))
                .flatMap(r -> matchResource(r, clientId)); // 匹配账户是否拥有权限
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
                .loadResourcePathByClientId(clientId)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied invalid client_id:" + clientId))))
                .map(paths -> paths.contains(routeDefinition.getId()));
    }

}
