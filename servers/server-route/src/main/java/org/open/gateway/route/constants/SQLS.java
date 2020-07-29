package org.open.gateway.route.constants;

import open.gateway.common.base.constants.GatewayConstants;
import open.gateway.common.utils.sql.SQL;

import java.util.Collection;

import static open.gateway.common.base.constants.GatewayConstants.IsDel;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
public interface SQLS {

    // 根据客户端id加载客户端信息
    SQL QUERY_CLIENT_BY_ID = new SQL() {{
        SELECT("t.client_id, t.client_secret, o.scope, o.web_server_redirect_uri, o.access_token_validity, o.refresh_token_validity, o.authorized_grant_types, o.autoapprove, o.authorities");
        FROM("gateway_app t");
        INNER_JOIN("oauth_client_details o on t.client_id = o.client_id");
        WHERE("t.is_del = " + GatewayConstants.IsDel.NO);
        WHERE("t.status = 1");
        WHERE("t.client_id = '%s'");
    }};

    // 查询所有路由信息
    SQL QUERY_API_ROUTE_DEFINITIONS = new SQL() {{
        SELECT("ga.id as api_id, ga.api_code, ga.api_path, gr.route_code, gr.route_path, gr.route_type, gr.url, gr.strip_prefix, gr.retryable, ga.is_auth, ga.is_open");
        FROM("gateway_route gr");
        INNER_JOIN("gateway_api ga on gr.route_code = ga.route_code");
        WHERE("gr.is_del = " + IsDel.NO);
        WHERE("ga.is_del = " + IsDel.NO);
    }};

    // 根据api_id查询限流信息
    SQL QUERY_RATE_LIMIT_BY_API_ID = new SQL() {{
        SELECT("grl.id, grl.limit_quota, grl.max_limit_quota, grl.interval_unit, grl.policy_type");
        FROM("gateway_rate_limit grl");
        INNER_JOIN("gateway_rate_limit_api grla on grl.id = grla.policy_id");
        WHERE("grl.is_del = " + IsDel.NO);
        WHERE("grla.api_id = %s");
    }};

    // 根据客户端查询api
    SQL QUERY_API_ROUTE = new SQL() {{
        SELECT("distinct gp.client_id, gr.route_path, ga.api_path");
        FROM("gateway_route gr");
        INNER_JOIN("gateway_api ga on gr.route_code = ga.route_code");
        INNER_JOIN("gateway_app_api gaa on gaa.api_id  = ga.id");
        INNER_JOIN("gateway_app gp on gp.id = gaa.app_id");
        WHERE("gr.is_del = " + IsDel.NO + " and ga.is_del = " + IsDel.NO + " and gp.is_del = " + IsDel.NO);
    }};

    static String QUERY_API_ROUTE_DEFINITIONS_BY_CODES(Collection<String> apiCodes) {
        if (apiCodes != null && !apiCodes.isEmpty()) {
            return SQLS.QUERY_API_ROUTE_DEFINITIONS.WHERE_IN("ga.api_code", apiCodes);
        }
        return SQLS.QUERY_API_ROUTE_DEFINITIONS.toString();
    }

    static String QUERY_API_ROUTE_BY_CLIENT_IDS(Collection<String> clientIds) {
        if (clientIds != null && !clientIds.isEmpty()) {
            return SQLS.QUERY_API_ROUTE.WHERE_IN("gp.client_id", clientIds);
        }
        return SQLS.QUERY_API_ROUTE.toString();
    }
}
