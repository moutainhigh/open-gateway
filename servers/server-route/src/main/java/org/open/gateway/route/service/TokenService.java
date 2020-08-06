package org.open.gateway.route.service;

import io.r2dbc.spi.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.utils.Dates;
import org.open.gateway.route.repositories.jdbc.SQLS;
import org.open.gateway.route.service.bo.OauthClientToken;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by miko on 2020/8/6.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private final TransactionalOperator operator;

    private final DatabaseClient databaseClient;

    /**
     * 根据客户端id获取token信息(不为null)
     *
     * @param clientId 客户端id
     * @return token信息
     */
    public Mono<OauthClientToken> loadClientTokenByClientId(String clientId) {
        return databaseClient.execute(SQLS.QUERY_CLIENT_TOKEN_BY_ID.getSql(clientId))
                .map(this::rowToClientToken)
                .one();
    }

    /**
     * 保存token
     *
     * @param clientId   客户端id
     * @param token      token值
     * @param expireTime 过期时间
     */
    public Mono<Void> saveClientToken(String clientId, String token, Long expireTime) {
        Assert.notNull(clientId, "client_id is required");
        Assert.notNull(token, "token is required");
        Assert.notNull(expireTime, "expireTime is required");
        return databaseClient.update()
                .table("oauth_client_token")
                .using(Update.update("is_del", 1))
                .matching(
                        Criteria.where("client_id").is(clientId).and("is_del").is(0)
                )
                .fetch().rowsUpdated() // 先根据客户端id更新老的token为已删除状态
                .then(
                        databaseClient.insert()
                                .into("oauth_client_token")
                                .value("client_id", clientId)
                                .value("token", token)
                                .value("expire_time", Dates.toLocalDateTime(expireTime))
                                .value("create_time", LocalDateTime.now())
                                .value("create_person", "system")
                                .value("update_time", LocalDateTime.now())
                                .value("update_person", "system")
                                .fetch()
                                .rowsUpdated() // 添加一条新的token记录
                                .then()
                )
                .as(operator::transactional)
                .doOnSuccess(v -> log.info("Save token finished")); // 开启事务管理
    }

    private OauthClientToken rowToClientToken(Row row) {
        OauthClientToken token = new OauthClientToken();
        token.setId(Objects.requireNonNull(row.get("id", Integer.class)));
        token.setClientId(Objects.requireNonNull(row.get("client_id", String.class)));
        token.setToken(Objects.requireNonNull(row.get("token", String.class)));
        token.setExpireTime(Objects.requireNonNull(row.get("expire_time", LocalDateTime.class)));
        return token;
    }

}

