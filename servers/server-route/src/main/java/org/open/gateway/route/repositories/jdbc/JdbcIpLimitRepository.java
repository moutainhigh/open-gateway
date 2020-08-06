package org.open.gateway.route.repositories.jdbc;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import open.gateway.common.base.entity.GatewayIpLimitDefinition;
import org.open.gateway.route.repositories.AbstractIpLimitRepository;
import org.open.gateway.route.utils.sql.Sql;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by miko on 2020/8/3.
 *
 * @author MIKO
 */
@AllArgsConstructor
public class JdbcIpLimitRepository extends AbstractIpLimitRepository {

    private final DatabaseClient databaseClient;

    @Override
    protected Flux<GatewayIpLimitDefinition.IpLimit> getIpLimits(Set<String> apiCodes) {
        return this.databaseClient.execute(SQLS.QUERY_IP_LIMIT.AND_IN("ga.api_code", apiCodes).getSql())
                .map(this::rowToIpLimit)
                .all();
    }

    private GatewayIpLimitDefinition.IpLimit rowToIpLimit(Row row) {
        GatewayIpLimitDefinition.IpLimit ipLimit = new GatewayIpLimitDefinition.IpLimit();
        ipLimit.setApiCode(row.get("api_code", String.class));
        ipLimit.setPolicyType(row.get("policy_type", String.class));
        ipLimit.setIpAddresses(new HashSet<>(Objects.requireNonNull(Sql.parseArrayField(row.get("ip_addresses", String.class)))));
        return ipLimit;
    }

    @Override
    public int refreshInterval() {
        return 60 * 60;
    }

}
