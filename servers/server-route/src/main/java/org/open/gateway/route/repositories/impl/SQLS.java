package org.open.gateway.route.repositories.impl;

import org.open.gateway.route.utils.sql.Sql;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
public interface SQLS {

    /**
     * 根据客户端id加载客户端信息
     */
    Sql QUERY_CLIENT_BY_ID = new Sql(
            "select t.client_id, t.client_secret, o.scope, o.web_server_redirect_uri, o.access_token_validity, o.refresh_token_validity, o.authorized_grant_types, o.autoapprove, o.authorities " +
                    "from gateway_app t " +
                    "inner join oauth_client_details o on t.client_id = o.client_id " +
                    "where t.is_del = 0 and t.status = 1 and t.client_id = '%s'"
    );

    /**
     * 根据客户端id加载客户端信息
     */
    Sql QUERY_CLIENT_TOKEN_BY_ID = new Sql(
            "select t.client_id, o.id, o.token, o.expire_time " +
                    "from gateway_app t " +
                    "inner join oauth_client_token o on t.client_id = o.client_id " +
                    "where t.is_del = 0 and t.status = 1 and o.is_del = 0 and t.client_id = '%s'"
    );

    /**
     * 查询所有路由信息
     */
    Sql QUERY_API_ROUTE_DEFINITIONS = new Sql(
            "select ga.id as api_id, ga.api_code, ga.api_path, gr.route_code, gr.route_path, gr.route_type, gr.url, gr.strip_prefix, gr.retryable, ga.is_auth, ga.is_open " +
                    "from gateway_route gr " +
                    "inner join gateway_api ga on gr.route_code = ga.route_code " +
                    "where gr.is_del = 0 and ga.is_del = 0"
    );

    /**
     * 根据api_id查询限流信息
     */
    Sql QUERY_RATE_LIMIT_BY_API_ID = new Sql(
            "select grl.id, grl.limit_quota, grl.max_limit_quota, grl.interval_unit, grl.policy_type " +
                    "from gateway_rate_limit grl " +
                    "inner join gateway_rate_limit_api grla on grl.id = grla.policy_id " +
                    "where grl.is_del = 0 and grla.api_id = %s"
    );

    /**
     * 根据客户端查询api
     */
    Sql QUERY_API_ROUTE = new Sql(
            "select distinct gp.client_id, gr.route_path, ga.api_path " +
                    "from gateway_route gr " +
                    "inner join gateway_api ga on gr.route_code = ga.route_code " +
                    "inner join gateway_app_api gaa on gaa.api_id  = ga.id " +
                    "inner join gateway_app gp on gp.id = gaa.app_id " +
                    "where gr.is_del = 0 and ga.is_del = 0 and gp.is_del = 0"
    );

    /**
     * 根据api查询ip限制策略
     */
    Sql QUERY_IP_LIMIT = new Sql(
            "select gil.policy_type, gil.ip_addresses, ga.api_code " +
                    "from gateway_ip_limit gil " +
                    "inner join gateway_ip_limit_api gila on gil.id = gila.policy_id " +
                    "inner join gateway_api ga on ga.id = gila.api_id " +
                    "where gil.is_del = 0 and gil.status = 1"
    );

}
