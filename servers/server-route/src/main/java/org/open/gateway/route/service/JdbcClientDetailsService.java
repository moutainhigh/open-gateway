package org.open.gateway.route.service;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.utils.CollectionUtil;
import org.open.gateway.route.exception.ClientNotFoundException;
import org.open.gateway.route.repositories.jdbc.SQLS;
import org.open.gateway.route.service.bo.BaseClientDetails;
import org.open.gateway.route.service.bo.ClientDetails;
import org.open.gateway.route.utils.sql.Sql;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/3.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class JdbcClientDetailsService implements ClientDetailsService {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<ClientDetails> loadClientByClientId(String clientId) {
        return databaseClient.execute(SQLS.QUERY_CLIENT_BY_ID.getSql(clientId))
                .map(this::rowToBaseClientDetails)
                .one()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ClientNotFoundException())));
    }

    private ClientDetails rowToBaseClientDetails(Row row) {
        BaseClientDetails result = new BaseClientDetails();
        result.setClientId(row.get("client_id", String.class));
        result.setClientSecret(row.get("client_secret", String.class));
        result.setAccessTokenValiditySeconds(row.get("access_token_validity", Integer.class));
        result.setRefreshTokenValiditySeconds(row.get("refresh_token_validity", Integer.class));
        result.setRegisteredRedirectUri(CollectionUtil.newHashSet(row.get("web_server_redirect_uri", String.class)));
        result.setAuthorizedGrantTypes(Sql.parseArrayField("authorized_grant_types"));
        result.setScope(Sql.parseArrayField(row.get("scope", String.class)));
        List<String> stringAuthorities = Sql.parseArrayField(row.get("authorities", String.class));
        if (stringAuthorities != null) {
            result.setAuthorities(stringAuthorities.stream()
                    .filter(authority -> !authority.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
        }
        return result;
    }

}
