package org.open.gateway.route.repositories;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.base.entity.RefreshGateway;
import org.open.gateway.route.entity.GatewayRateLimitDefinition;
import org.open.gateway.route.entity.GatewayRouteDefinition;
import org.open.gateway.route.utils.PathUtil;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by miko on 2020/6/9.
 *
 * @author MIKO
 */
@Slf4j
public abstract class AbstractRouteDefinitionRepository implements RefreshableRouteDefinitionRepository {

    private static final String DEFAULT_KEY_RESOLVER = "urlKeyResolver";
    private final Map<String, RouteDefinition> routes = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            if (StringUtils.isEmpty(r.getId())) {
                return Mono.error(new IllegalArgumentException("id may not be empty"));
            }
            routes.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(
                    new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    @Override
    public Mono<RouteDefinition> loadRouteDefinition(String path) {
        return Mono.justOrEmpty(routes.get(path));
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        // 需要更新的api
        Set<String> refreshApiCodes = param == null ? null : param.getArgs();
        // 清理路由配置
        Mono<Void> clearRoutes = Mono.fromRunnable(() -> clearRoutes(refreshApiCodes));
        // 读取并保存路由配置
        Mono<Void> readAndSaveRoute = getRefreshableRouteDefinitions(refreshApiCodes)
                .map(this::toRouteDefinition)
                .flatMap(routeDefinition -> this.save(Mono.just(routeDefinition)))
                .then();
        return clearRoutes
                .then(readAndSaveRoute)
                .doOnSubscribe(v -> log.info("[Refresh routes] starting. target api codes:{}", refreshApiCodes))
                .doOnSuccess(v -> log.info("[Refresh routes] finished"))
                .doOnError(e -> log.error("[Refresh routes] error:" + e.getMessage()));
    }

    /**
     * 清理路由配置
     */
    private void clearRoutes(Set<String> apiCodes) {
        if (apiCodes == null) {
            this.routes.clear();
            log.info("[Refresh routes] clear all routes finished");
        } else {
            if (!apiCodes.isEmpty()) {
                this.routes.values().removeIf(route -> apiCodes.contains(RouteDefinitionUtil.getApiCode(route)));
                log.info("[Refresh routes] clear routes finished");
            }
        }
    }

    /**
     * 获取网关路由配置信息
     *
     * @return 路由配置信息
     */
    protected abstract Flux<GatewayRouteDefinition> getRefreshableRouteDefinitions(Set<String> apiCodes);

    /**
     * 网关配置信息转路由配置
     *
     * @param route 网关配置信息
     * @return 路由配置
     */
    protected RouteDefinition toRouteDefinition(GatewayRouteDefinition route) {
        // 校验路由信息
        checkRouteInfo(route);
        // 服务地址
        URI uri = UriComponentsBuilder.fromUriString(route.getUrl()).build().toUri();
        // 路由转发配置
        PredicateDefinition predicatePath = getPathPredicateDefinition(route);
        // 过滤器
        List<FilterDefinition> filters = new ArrayList<>();
        // 转发时忽略前缀过滤器
        filters.add(getStripPrefixFilterDefinition(route));
        // 限流过滤器
        if (route.getRateLimit() != null) {
            filters.add(getRateLimitFilterDefinition(route.getRateLimit()));
        }
        // 路由配置
        RouteDefinition definition = new RouteDefinition();
        definition.setId(PathUtil.getFullPath(route.getRoutePath(), route.getApiPath()));
        definition.setUri(uri);
        definition.setPredicates(Arrays.asList(predicatePath));
        definition.setFilters(filters);
        setMetadata(definition, route);
        return definition;
    }

    /**
     * 配置权限信息
     *
     * @param definition 路由规则
     * @param route      路由配置
     */
    private void setMetadata(RouteDefinition definition, GatewayRouteDefinition route) {
        Map<String, Object> metadata = new HashMap<>();
        definition.setMetadata(metadata);
        RouteDefinitionUtil.setApiCode(definition, route.getApiCode());
        RouteDefinitionUtil.setRouteCode(definition, route.getRouteCode());
        RouteDefinitionUtil.setIsAuth(definition, route.isAuth());
        RouteDefinitionUtil.setIsOpen(definition, route.isOpen());
    }

    /**
     * 路径转发配置
     *
     * @param route 路由信息
     * @return 转发配置
     */
    private PredicateDefinition getPathPredicateDefinition(GatewayRouteDefinition route) {
        String path = PathUtil.getFullPath(route.getRoutePath(), route.getApiPath());
        PredicateDefinition predicatePath = new PredicateDefinition();
        Map<String, String> predicatePathParams = new HashMap<>();
        predicatePathParams.put("name", route.getApiCode());
        predicatePathParams.put("pattern", path);
        predicatePathParams.put("pathPattern", path);
        predicatePath.setArgs(predicatePathParams);
        predicatePath.setName("Path");
        return predicatePath;
    }

    /**
     * 获取前缀过滤器
     *
     * @param route 路由配置
     * @return 前缀过滤器
     */
    private FilterDefinition getStripPrefixFilterDefinition(GatewayRouteDefinition route) {
        FilterDefinition stripPrefixDefinition = new FilterDefinition();
        Map<String, String> stripPrefixParams = new HashMap<>();
        stripPrefixParams.put(NameUtils.generateName(0), getStripPrefix(route));
        stripPrefixDefinition.setArgs(stripPrefixParams);
        stripPrefixDefinition.setName("StripPrefix");
        return stripPrefixDefinition;
    }

    /**
     * 获取限流过滤器
     *
     * @param rateLimit 限流配置
     * @return 限流过滤器
     */
    private FilterDefinition getRateLimitFilterDefinition(GatewayRateLimitDefinition rateLimit) {
        // 获取每次请求耗费token的数量
        long requestedTokens = rateLimit.requestedTokens();
        // 允许用户每秒处理多少个请求
        long replenishRate = rateLimit.getLimitQuota();
        // 令牌桶的容量
        long burstCapacity = requestedTokens * rateLimit.getMaxLimitQuota();
        // 限流
        FilterDefinition rateLimiterDefinition = new FilterDefinition();
        Map<String, String> rateLimiterParams = new HashMap<>(8);
        rateLimiterDefinition.setName("RequestRateLimiter");
        //令牌桶流速
        rateLimiterParams.put("redis-rate-limiter.replenishRate", String.valueOf(replenishRate));
        //令牌桶容量
        rateLimiterParams.put("redis-rate-limiter.burstCapacity", String.valueOf(burstCapacity));
        //请求耗费的令牌数量
        rateLimiterParams.put("redis-rate-limiter.requestedTokens", String.valueOf(requestedTokens));
        // 限流策略(#{@BeanName})
        rateLimiterParams.put("key-resolver", getKeyResolver(rateLimit.getPolicyType()));
        rateLimiterDefinition.setArgs(rateLimiterParams);
        return rateLimiterDefinition;
    }

    /**
     * 获取限流策略实现
     *
     * @param policyType 限流策略
     * @return 限流策略实现类
     */
    private String getKeyResolver(String policyType) {
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_URL.equals(policyType)) {
            return "#{@urlKeyResolver}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_USER.equals(policyType)) {
            return "#{@userKeyResolver}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_URL_USER.equals(policyType)) {
            return "#{@urlUserKeyResolver}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_IP.equals(policyType)) {
            return "#{@ipKeyResolver}";
        }
        log.warn("Invalid policy type:{} use default key resolver:{}", policyType, DEFAULT_KEY_RESOLVER);
        return "#{" + DEFAULT_KEY_RESOLVER + "}";
    }

    /**
     * 校验路由信息
     *
     * @param route 路由信息
     */
    private void checkRouteInfo(GatewayRouteDefinition route) {
        Assert.notNull(route.getUrl(), "Url can not be null");
        Assert.notNull(route.getApiCode(), "ApiCode can not be null");
        Assert.notNull(route.getApiPath(), "ApiPath can not be null");
        Assert.notNull(route.getRouteCode(), "RouteCode can not be null");
        Assert.notNull(route.getRoutePath(), "RoutePath can not be null");
    }

    /**
     * 获取忽略前缀的个数
     *
     * @param route 路由信息
     * @return 忽略前缀的个数
     */
    private String getStripPrefix(GatewayRouteDefinition route) {
        if (route.getStripPrefix() != null) {
            return route.getStripPrefix().toString();
        } else {
            return "1";
        }
    }

}
