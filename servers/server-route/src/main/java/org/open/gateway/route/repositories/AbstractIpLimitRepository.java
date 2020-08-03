package org.open.gateway.route.repositories;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.entity.GatewayIpLimitDefinition;
import open.gateway.common.base.entity.RefreshGateway;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * Created by miko on 2020/8/3.
 *
 * @author MIKO
 */
@Slf4j
public abstract class AbstractIpLimitRepository implements RefreshableIpLimitRepository {

    private final Map<String, GatewayIpLimitDefinition> ipLimits = synchronizedMap(new LinkedHashMap<>());

    @Override
    public Mono<GatewayIpLimitDefinition> loadIpLimitByApi(String apiCode) {
        return Mono.justOrEmpty(ipLimits.get(apiCode));
    }

    @Override
    public Mono<Void> refresh(RefreshGateway param) {
        // 需要更新的api
        Set<String> refreshApiCodes = param == null ? null : param.getArgs();
        // 清理ip限制
        Mono<Void> clearIpLimits = Mono.fromRunnable(() -> this.clearIpLimits(refreshApiCodes));
        return clearIpLimits.then(
                getIpLimits(refreshApiCodes)
                        .collect(Collectors.groupingBy(GatewayIpLimitDefinition.IpLimit::getApiCode))
                        .doOnSuccess(group -> {
                            group.forEach((key, value) -> this.ipLimits.put(key, new GatewayIpLimitDefinition(value)));
                            log.info("Refresh ip limits finished");
                        })
                        .doOnError(e -> log.error("Refresh ip limits failed reason:{}", e.getMessage()))
                        .then()
        );
    }

    private void clearIpLimits(Set<String> apiCodes) {
        if (apiCodes == null) {
            ipLimits.clear();
        } else {
            apiCodes.forEach(ipLimits::remove);
        }
    }

    protected abstract Flux<GatewayIpLimitDefinition.IpLimit> getIpLimits(Set<String> apiCode);

}
