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
                    "where t.status = 1 and t.client_id = '%s' "
    );

    /**
     * 根据客户端id加载客户端token
     */
    Sql QUERY_CLIENT_TOKEN_BY_ID = new Sql(
            "select o.id, o.token, o.expire_time " +
                    "from gateway_app t " +
                    "inner join oauth_client_token o on t.client_id = o.client_id " +
                    "where t.status = 1 and t.client_id = '%s' and o.expire_time > now() " +
                    "order by o.expire_time desc limit 1 "
    );

    /**
     * 查询所有路由信息
     */
    Sql QUERY_API_ROUTE_DEFINITIONS = new Sql(
            "select ga.id as api_id, ga.api_code, ga.api_path, gr.route_code, gr.route_path, gr.route_type, gr.url, gr.strip_prefix, ga.retry_times, ga.is_auth, " +
                    "t.id as rate_limit_id, t.limit_quota, t.max_limit_quota, t.interval_unit, t.policy_type " +
                    "from gateway_route gr " +
                    "inner join gateway_api ga on gr.route_code = ga.route_code " +
                    "left join ( " +
                    "select grla.api_id, grl.* from gateway_rate_limit_api grla inner join gateway_rate_limit grl on grla.policy_id = grl.id where grl.status = 1 " +
                    ") as t on t.api_id = ga.id " +
                    "where gr.status = 1 and ga.status = 1 "
    );

    /**
     * 根据api_id查询分组信息
     */
    Sql QUERY_GROUP_CODES_BY_API_ID = new Sql(
            "select gg.group_code from gateway_api ga " +
                    "inner join gateway_group_api gga on ga.id = gga.api_id " +
                    "inner join gateway_group gg on gga.group_id = gg.id " +
                    "where ga.status = 1 and gga.api_id = %s "
    );

    /**
     * 根据api查询ip限制策略
     */
    Sql QUERY_IP_LIMIT = new Sql(
            "select gil.policy_type, gil.ip_addresses, ga.api_code " +
                    "from gateway_ip_limit gil " +
                    "inner join gateway_ip_limit_api gila on gil.id = gila.policy_id " +
                    "inner join gateway_api ga on ga.id = gila.api_id " +
                    "where gil.status = 1 and ga.status = 1 "
    );

}
