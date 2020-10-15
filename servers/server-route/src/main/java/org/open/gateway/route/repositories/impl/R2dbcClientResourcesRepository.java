package org.open.gateway.route.repositories.impl;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import org.open.gateway.route.entity.GatewayClientResourceDefinition;
import org.open.gateway.route.repositories.AbstractClientResourcesRepository;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.util.Set;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class R2dbcClientResourcesRepository extends AbstractClientResourcesRepository {

    private final DatabaseClient databaseClient;

    @Override
    protected Flux<GatewayClientResourceDefinition> getClientApiRoutes(Set<String> clientIds) {
        return this.databaseClient
                .execute(SQLS.QUERY_API_ROUTE.andIn("gp.client_id", clientIds).format())
                .map(this::rowToClientResource)
                .all();
    }

    private GatewayClientResourceDefinition rowToClientResource(Row row) {
        GatewayClientResourceDefinition clientResource = new GatewayClientResourceDefinition();
        clientResource.setClientId(row.get("client_id", String.class));
        clientResource.setRoutePath(row.get("route_path", String.class));
        clientResource.setApiPath(row.get("api_path", String.class));
        return clientResource;
    }

}
