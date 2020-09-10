package org.open.gateway.route.repositories.jdbc;

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
public class JdbcClientResourcesRepository extends AbstractClientResourcesRepository {

    private final DatabaseClient databaseClient;

    @Override
    public int refreshInterval() {
        return 60 * 60;
    }

    @Override
    protected Flux<GatewayClientResourceDefinition.ClientResource> getClientApiRoutes(Set<String> clientIds) {
        return this.databaseClient
                .execute(SQLS.QUERY_API_ROUTE.AND_IN("gp.client_id", clientIds).getSql())
                .map(this::rowToClientResource)
                .all();
    }

    private GatewayClientResourceDefinition.ClientResource rowToClientResource(Row row) {
        GatewayClientResourceDefinition.ClientResource clientResource = new GatewayClientResourceDefinition.ClientResource();
        clientResource.setClientId(row.get("client_id", String.class));
        clientResource.setClientSecret(row.get("client_secret", String.class));
        clientResource.setRoutePath(row.get("route_path", String.class));
        clientResource.setApiPath(row.get("api_path", String.class));
        return clientResource;
    }

}
