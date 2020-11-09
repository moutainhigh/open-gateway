package org.open.gateway.portal.constants;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
public interface Endpoints {

    // 用户-登录
    String ACCOUNT_LOGIN = "/account/login";
    // 用户-注册
    String ACCOUNT_REGISTER = "/account/register";
    // 用户-退出登录
    String ACCOUNT_LOGOUT = "/account/logout";
    // 用户-修改
    String ACCOUNT_UPDATE = "/account/update";
    // 用户-启用
    String ACCOUNT_ENABLE = "/account/enable";
    // 用户-禁用
    String ACCOUNT_DISABLE = "/account/disable";
    // 用户-删除
    String ACCOUNT_DELETE = "/account/delete";
    // 用户-分页列表
    String ACCOUNT_PAGES = "/account/pages";
    // 用户-资源列表
    String ACCOUNT_RESOURCES = "/account/resources";

    // 角色-分页列表
    String ROLE_PAGES = "/role/pages";
    // 角色-新增/修改
    String ROLE_SAVE = "/role/save";
    // 角色-启用
    String ROLE_ENABLE = "/role/enable";
    // 角色-禁用
    String ROLE_DISABLE = "/role/disable";
    // 角色-删除
    String ROLE_DELETE = "/role/delete";
    // 角色-资源列表
    String ROLE_RESOURCES = "/role/resources";

    // 用户-资源列表(所有)
    String RESOURCE_LIST = "/resource/list";
    // 用户-资源新增/修改
    String RESOURCE_SAVE = "/resource/save";
    // 用户-资源删除
    String RESOURCE_DELETE = "/resource/delete";

    // 网关-路由刷新
    String GATEWAY_REFRESH_ROUTE = "/gateway/refresh/route";
    // 网关-资源刷新
    String GATEWAY_REFRESH_CLIENT_TOKEN = "/gateway/refresh/clientToken";
    // 网关-限流刷新
    String GATEWAY_REFRESH_IP_LIMIT = "/gateway/refresh/ipLimit";

    // 应用-分页列表
    String APP_PAGES = "/app/pages";
    // 应用-新增/修改
    String APP_SAVE = "/app/save";
    // 应用-启用
    String APP_ENABLE = "/app/enable";
    // 应用-禁用
    String APP_DISABLE = "/app/disable";
    // 应用-删除
    String APP_DELETE = "/app/delete";

    // 路由-分页列表
    String ROUTES_PAGES = "/gateway/route/pages";
    // 路由-列表(所有)
    String ROUTES_LIST = "/gateway/route/list";
    // 路由-新增/修改
    String ROUTES_SAVE = "/gateway/route/save";
    // 路由-删除
    String ROUTES_DELETE = "/gateway/route/delete";
    // 路由-查询所有类型
    String ROUTES_TYPES = "/gateway/route/types";

    // 接口-分页列表
    String API_PAGES = "/api/pages";
    // 接口-新增/修改
    String API_SAVE = "/api/save";
    // 接口-删除
    String API_DELETE = "/api/delete";

    // 接口分类-列表
    String API_CLASSIFICATION = "/api/classification/list";
    // 接口分类-新增/修改
    String API_CLASSIFICATION_SAVE = "/api/classification/save";
    // 接口分类-删除
    String API_CLASSIFICATION_DELETE = "/api/classification/delete";

    // 策略-限流-分页列表
    String POLICY_RATE_LIMIT_PAGES = "/policy/rateLimit/pages";
    // 策略-限流-新增/修改
    String POLICY_RATE_LIMIT_SAVE = "/policy/rateLimit/save";
    // 策略-限流-删除
    String POLICY_RATE_LIMIT_DELETE = "/policy/rateLimit/delete";
    // 策略-限流-更新所拥有的接口
    String POLICY_RATE_LIMIT_SAVE_APIS = "/policy/rateLimit/save/apis";
    // 策略-限流-查询所有类型
    String POLICY_RATE_LIMIT_TYPES = "/policy/rateLimit/types";

    // 策略-黑白名单-分页列表
    String POLICY_IP_LIMIT_PAGES = "/policy/ipLimit/pages";
    // 策略-黑白名单-新增/修改
    String POLICY_IP_LIMIT_PAGE_SAVE = "/policy/ipLimit/save";
    // 策略-黑白名单-删除
    String POLICY_IP_LIMIT_PAGE_DELETE = "/policy/ipLimit/delete";
    // 策略-黑白名单-更新所拥有的接口
    String POLICY_IP_LIMIT_PAGE_SAVE_APIS = "/policy/ipLimit/save/apis";
    // 策略-黑白名单-查询所有类型
    String POLICY_IP_LIMIT_TYPES = "/policy/ipLimit/types";

    // 监控-日志-分页列表
    String MONITOR_LOGS_PAGES = "/monitor/accessLogs/pages";

}
