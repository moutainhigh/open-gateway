package org.open.gateway.route.repositories.jdbc;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import org.open.gateway.route.entity.GatewayRateLimitDefinition;
import org.open.gateway.route.entity.GatewayRouteDefinition;
import org.open.gateway.route.repositories.AbstractRouteDefinitionRepository;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Set;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class JdbcRouteDefinitionRepository extends AbstractRouteDefinitionRepository {

    private final DatabaseClient databaseClient;
    private final int refreshInterval;

    @Override
    public int refreshInterval() {
        return this.refreshInterval;
    }

    @Override
    protected Flux<GatewayRouteDefinition> getRefreshableRouteDefinitions(Set<String> apiCodes) {
        return this.databaseClient.execute(SQLS.QUERY_API_ROUTE_DEFINITIONS.AND_IN("ga.api_code", apiCodes).getSql())
                .map(this::rowToGatewayRouteDefinition)
                .all()
                .flatMap(routeDefinition ->
                        this.databaseClient
                                .execute(SQLS.QUERY_RATE_LIMIT_BY_API_ID.getSql(routeDefinition.getApiId()))
                                .map(this::rowToGatewayRateLimitDefinition)
                                .one()
                                .doOnSuccess(routeDefinition::setRateLimit)
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
        routeDefinition.setRetryable(row.get("retryable", Integer.class));
        routeDefinition.setAuth(Objects.requireNonNull(row.get("is_auth", Integer.class)) == 1);
        routeDefinition.setOpen(Objects.requireNonNull(row.get("is_open", Integer.class)) == 1);
        return routeDefinition;
    }

    private GatewayRateLimitDefinition rowToGatewayRateLimitDefinition(Row row) {
        GatewayRateLimitDefinition rateLimitDefinition = new GatewayRateLimitDefinition();
        rateLimitDefinition.setId(row.get("id", Long.class));
        rateLimitDefinition.setPolicyType(row.get("policy_type", String.class));
        rateLimitDefinition.setLimitQuota(row.get("limit_quota", Long.class));
        rateLimitDefinition.setMaxLimitQuota(row.get("max_limit_quota", Long.class));
        rateLimitDefinition.setIntervalUnit(row.get("interval_unit", String.class));
        return rateLimitDefinition;
    }

}
