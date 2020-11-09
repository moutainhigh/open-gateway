package org.open.gateway.route.repositories.impl;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import org.open.gateway.route.entity.GatewayRateLimitDefinition;
import org.open.gateway.route.entity.GatewayRouteDefinition;
import org.open.gateway.route.repositories.AbstractRouteDefinitionRepository;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
@Component
@AllArgsConstructor
public class R2dbcRouteDefinitionRepository extends AbstractRouteDefinitionRepository {

    private final DatabaseClient databaseClient;

    @Override
    protected Flux<GatewayRouteDefinition> getRefreshableRouteDefinitions(Set<String> apiCodes) {
        return this.databaseClient.execute(SQLS.QUERY_API_ROUTE_DEFINITIONS.whereIn("ga.api_code", apiCodes).format())
                .map(this::rowToGatewayRouteDefinition)
                .all()
                .flatMap(routeDefinition ->
                        this.databaseClient
                                .execute(SQLS.QUERY_GROUP_CODES_BY_API_ID.format(routeDefinition.getApiId()))
                                .map(row -> row.get("group_code", String.class))
                                .all()
                                .collect(Collectors.toSet())
                                .doOnSuccess(routeDefinition::setGroupCodes)
                                .thenReturn(routeDefinition)
                );
    }

    private GatewayRouteDefinition rowToGatewayRouteDefinition(Row row) {
        GatewayRouteDefinition routeDefinition = new GatewayRouteDefinition();
        routeDefinition.setApiId(row.get("api_id", Long.class));
        routeDefinition.setApiCode(row.get("api_code", String.class));
        routeDefinition.setApiPath(row.get("api_path", String.class));
        routeDefinition.setRouteCode(row.get("route_code", String.class));
        routeDefinition.setRoutePath(row.get("route_path", String.class));
        routeDefinition.setRouteType(row.get("route_type", Integer.class));
        routeDefinition.setUrl(row.get("url", String.class));
        routeDefinition.setStripPrefix(row.get("strip_prefix", Integer.class));
        routeDefinition.setRetryTimes(row.get("retry_times", Integer.class));
        routeDefinition.setAuth(Objects.requireNonNull(row.get("is_auth", Integer.class)) == 1);
        setGatewayRateLimitDefinition(routeDefinition, row);
        return routeDefinition;
    }

    private void setGatewayRateLimitDefinition(GatewayRouteDefinition routeDefinition, Row row) {
        Long rateLimitId = row.get("rate_limit_id", Long.class);
        if (rateLimitId != null) {
            GatewayRateLimitDefinition rateLimitDefinition = new GatewayRateLimitDefinition();
            rateLimitDefinition.setId(row.get("rate_limit_id", Long.class));
            rateLimitDefinition.setPolicyType(row.get("policy_type", String.class));
            rateLimitDefinition.setLimitQuota(row.get("limit_quota", Long.class));
            rateLimitDefinition.setMaxLimitQuota(row.get("max_limit_quota", Long.class));
            rateLimitDefinition.setIntervalUnit(row.get("interval_unit", String.class));
            routeDefinition.setRateLimit(rateLimitDefinition);
        }
    }

}
