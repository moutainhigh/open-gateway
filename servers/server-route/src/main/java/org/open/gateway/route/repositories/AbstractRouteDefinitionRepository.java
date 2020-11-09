package org.open.gateway.route.repositories;

import org.open.gateway.base.constants.GatewayConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.common.utils.UrlUtil;
import org.open.gateway.route.entity.GatewayRateLimitDefinition;
import org.open.gateway.route.entity.GatewayRouteDefinition;
import org.open.gateway.route.utils.RouteDefinitionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpMethod;
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
public abstract class AbstractRouteDefinitionRepository implements RefreshableRouteDefinitionRepository, ApplicationEventPublisherAware {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 路由信息
     */
    private final Map<String, RouteDefinition> routes = new ConcurrentHashMap<>();
    /**
     * 事件发布器
     */
    private ApplicationEventPublisher eventPublisher;

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
        Set<String> refreshApiCodes = param.getArgs();
        return getRefreshableRouteDefinitions(refreshApiCodes) // 读取网关配置
                .flatMap(definition ->
                        Mono.just(definition).map(this::toRouteDefinition)
                                .onErrorResume(ex -> {
                                    log.error("[Refresh routes] update route code:[{}] failed. error:{}", definition.getRouteCode(), ex.getMessage());
                                    return Mono.empty();
                                }) // 忽略错误的路由配置
                ) // 转换为spring cloud gateway配置对象
                .collectMap(RouteDefinition::getId) // 转换为map结构,key为路由id
                .doOnNext(rts -> {
                    log.info("[Refresh routes] route definition num:{}", rts.size());
                    if (rts.size() > 0) {
                        clearRoutes(param); // 清理路由配置
                        this.routes.putAll(rts); // 更新内存路由配置
                    }
                })
                .then()
                .doOnSuccess(v -> publishEvent())
                .doOnSubscribe(v -> log.info("[Refresh routes] starting. target api codes:{}", refreshApiCodes))
                .doOnError(e -> log.error("[Refresh routes] error:" + e.getMessage()));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    /**
     * 刷新路由
     */
    private void publishEvent() {
        // 发布刷新路由事件
        this.eventPublisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("[Refresh routes] Publish refresh routes event finished");
        log.info("[Refresh routes] finished");
    }

    /**
     * 清理路由配置
     */
    private void clearRoutes(RefreshGateway param) {
        if (this.routes.isEmpty()) {
            return;
        }
        if (param.isRefreshAll()) {
            // 清理所有路由配置
            this.routes.clear();
            log.info("[Refresh routes] clear routes finished");
        } else {
            // 按照api code清理路由配置
            this.routes.values().removeIf(route -> param.getArgs().contains(RouteDefinitionUtil.getApiCode(route)));
            log.info("[Refresh routes] remove old routes finished");
        }
    }

    /**
     * 网关配置信息转路由配置
     *
     * @param route 网关配置信息
     * @return 路由配置
     */
    protected RouteDefinition toRouteDefinition(GatewayRouteDefinition route) {
        // 校验路由信息
        checkRouteInfo(route);
        // 过滤器
        List<FilterDefinition> filters = new ArrayList<>();
        // 前缀过滤器
        if (route.getStripPrefix() != null && route.getStripPrefix() > 0) {
            filters.add(getStripPrefixFilterDefinition(route));
        }
        // 重试过滤器
        if (route.getRetryTimes() != null && route.getRetryTimes() > 0) {
            filters.add(getRetryFilterDefinition(route));
        }
        // 限流过滤器
        if (route.getRateLimit() != null) {
            filters.add(getRateLimitFilterDefinition(route.getRateLimit()));
        }
        // 路由转发配置
        PredicateDefinition pathPredicate = getPathPredicateDefinition(route);
        // 服务地址
        URI uri = UriComponentsBuilder.fromUriString(route.getUrl()).build().toUri();
        // 路由配置
        RouteDefinition definition = new RouteDefinition();
        definition.setId(UrlUtil.appendUrlPath(route.getRoutePath(), route.getApiPath()));
        definition.setUri(uri);
        definition.setPredicates(Arrays.asList(pathPredicate));
        definition.setFilters(filters);
        setMetadata(definition, route);
        return definition;
    }

    /**
     * 配置额外信息
     *
     * @param definition 路由规则
     * @param route      路由配置
     */
    private void setMetadata(RouteDefinition definition, GatewayRouteDefinition route) {
        RouteDefinitionUtil.setApiCode(definition, route.getApiCode());
        RouteDefinitionUtil.setGroupCodes(definition, route.getGroupCodes());
        RouteDefinitionUtil.setRouteCode(definition, route.getRouteCode());
        RouteDefinitionUtil.setIsAuth(definition, route.isAuth());
    }

    /**
     * 路径转发配置
     *
     * @param route 路由信息
     * @return 转发配置
     */
    private PredicateDefinition getPathPredicateDefinition(GatewayRouteDefinition route) {
        String path = UrlUtil.appendUrlPath(route.getRoutePath(), route.getApiPath());
        PredicateDefinition definition = new PredicateDefinition();
        Map<String, String> params = new HashMap<>(4);
        params.put("name", route.getApiCode());
        params.put("pattern", path);
        params.put("pathPattern", path);
        definition.setArgs(params);
        definition.setName("Path");
        return definition;
    }

    /**
     * 获取前缀过滤器
     *
     * @param route 路由配置
     * @return 前缀过滤器
     */
    private FilterDefinition getStripPrefixFilterDefinition(GatewayRouteDefinition route) {
        FilterDefinition definition = new FilterDefinition("StripPrefix");
        Map<String, String> params = new HashMap<>(2);
        params.put(NameUtils.generateName(0), getStripPrefix(route));
        definition.setArgs(params);
        return definition;
    }

    /**
     * 获取重试过滤器
     *
     * @param route 路由配置
     * @return 重试过滤器
     */
    private FilterDefinition getRetryFilterDefinition(GatewayRouteDefinition route) {
        FilterDefinition definition = new FilterDefinition("Retry");
        Map<String, String> params = new HashMap<>(4);
        // 重试次数
        params.put("retries", String.valueOf(route.getRetryTimes()));
        // 重试的http请求方式
        params.put("methods", HttpMethod.GET.name() + "," + HttpMethod.POST.name());
        definition.setArgs(params);
        return definition;
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
        FilterDefinition definition = new FilterDefinition("RequestRateLimiter");
        Map<String, String> params = new HashMap<>(8);
        // 令牌桶流速
        params.put("redis-rate-limiter.replenishRate", String.valueOf(replenishRate));
        // 令牌桶容量
        params.put("redis-rate-limiter.burstCapacity", String.valueOf(burstCapacity));
        // 请求耗费的令牌数量
        params.put("redis-rate-limiter.requestedTokens", String.valueOf(requestedTokens));
        // 限流策略(#{@BeanName})
        params.put("key-resolver", getKeyResolver(rateLimit.getPolicyType()));
        definition.setArgs(params);
        return definition;
    }

    /**
     * 获取限流策略实现
     *
     * @param policyType 限流策略
     * @return 限流策略实现类
     */
    private String getKeyResolver(String policyType) {
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_URL.equals(policyType)) {
            return "#{@" + GatewayConstants.RateLimitPolicy.KEY_RESOLVER_URL + "}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_USER.equals(policyType)) {
            return "#{@" + GatewayConstants.RateLimitPolicy.KEY_RESOLVER_USER + "}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_URL_USER.equals(policyType)) {
            return "#{@" + GatewayConstants.RateLimitPolicy.KEY_RESOLVER_URL_USER + "}";
        }
        if (GatewayConstants.RateLimitPolicy.POLICY_TYPE_IP.equals(policyType)) {
            return "#{@" + GatewayConstants.RateLimitPolicy.KEY_RESOLVER_IP + "}";
        }
        log.warn("Invalid policy type:{} use default key resolver:{}", policyType, GatewayConstants.RateLimitPolicy.KEY_RESOLVER_URL);
        return "#{@" + GatewayConstants.RateLimitPolicy.KEY_RESOLVER_URL + "}";
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
        return String.valueOf(route.getStripPrefix());
    }

    /**
     * 获取网关路由配置信息
     *
     * @param apiCodes api代码
     * @return 路由配置信息
     */
    protected abstract Flux<GatewayRouteDefinition> getRefreshableRouteDefinitions(Set<String> apiCodes);

}
