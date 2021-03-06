package org.open.gateway.route.repositories;

import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.route.entity.GatewayIpLimitDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/8/3.
 *
 * @author MIKO
 */
public abstract class AbstractIpLimitRepository implements RefreshableIpLimitRepository {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Map<String, GatewayIpLimitDefinition> ipLimits = new ConcurrentHashMap<>();

    @Override
    public Mono<GatewayIpLimitDefinition> loadIpLimitByApi(String apiCode) {
        return Mono.justOrEmpty(this.ipLimits.get(apiCode));
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        // 需要更新的api
        Set<String> refreshApiCodes = param.getArgs();
        return getIpLimits(refreshApiCodes)
                .collect(Collectors.groupingBy(GatewayIpLimitDefinition.IpLimit::getApiCode))
                .doOnNext(group -> {
                    log.info("[Refresh ip limits] ip limit definition num:{}", group.size());
                    if (group.size() > 0) {
                        this.clearIpLimits(param); // 清理ip限制
                        group.forEach((key, value) -> this.ipLimits.put(key, new GatewayIpLimitDefinition(value)));
                    }
                })
                .doOnSubscribe(v -> log.info("[Refresh ip limits] starting. target api codes:{}", refreshApiCodes))
                .doOnSuccess(group -> log.info("[Refresh ip limits] finished"))
                .doOnError(e -> log.error("[Refresh ip limits] failed reason:{}", e.getMessage()))
                .then();
    }

    /**
     * 根据api代码清理黑白名单
     *
     * @param param api编码
     */
    private void clearIpLimits(RefreshGateway param) {
        if (this.ipLimits.isEmpty()) {
            return;
        }
        if (param.isRefreshAll()) {
            this.ipLimits.clear();
            log.info("[Refresh ip limits] clear ip limits finished");
        } else {
            param.getArgs().forEach(this.ipLimits::remove);
            log.info("[Refresh ip limits] remove old ip limits finished");
        }
    }

    /**
     * 根据api代码获取黑白名单
     *
     * @param apiCodes api编码
     * @return 黑白名单配置
     */
    protected abstract Flux<GatewayIpLimitDefinition.IpLimit> getIpLimits(Set<String> apiCodes);

}
