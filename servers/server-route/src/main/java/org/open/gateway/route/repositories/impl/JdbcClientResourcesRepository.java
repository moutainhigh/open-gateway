package org.open.gateway.route.repositories.impl;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import open.gateway.common.base.entity.ClientResource;
import org.open.gateway.route.constants.SQLS;
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
        return 300;
    }

    @Override
    protected Flux<ClientResource> getClientApiRoutes(Set<String> clientIds) {
        return this.databaseClient
                .execute(SQLS.QUERY_API_ROUTE_BY_CLIENT_IDS(clientIds))
                .map(this::rowToClientResource)
                .all();
    }

    private ClientResource rowToClientResource(Row row) {
        ClientResource clientResource = new ClientResource();
        clientResource.setClientId(row.get("client_id", String.class));
        clientResource.setRoutePath(row.get("route_path", String.class));
        clientResource.setApiPath(row.get("api_path", String.class));
        return clientResource;
    }

}
