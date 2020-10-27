package org.open.gateway.portal.constants;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
public interface EndPoints {

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
    // 用户-资源列表(所有)
    String ACCOUNT_RESOURCE_LIST = "/account/resource/list";
    // 用户-角色列表
    String ACCOUNT_ROLE_PAGES = "/account/role/pages";
    // 用户-角色新增/修改
    String ACCOUNT_ROLE_SAVE = "/account/role/save";

    // 网关-路由刷新
    String GATEWAY_ROUTES_REFRESH = "/gateway/routes/refresh";
    // 网关-资源刷新
    String GATEWAY_RESOURCES_REFRESH = "/gateway/resources/refresh";
    // 网关-限流刷新
    String GATEWAY_IP_LIMITS_REFRESH = "/gateway/ipLimits/refresh";

    // 日志-分页列表
    String ACCESS_LOGS_PAGES = "/accessLogs/pages";

    // 路由-分页列表
    String ROUTES_PAGES = "/routes/pages";
    // 路由-列表(所有)
    String ROUTES_LIST = "/routes/list";
    // 路由-新增/修改
    String ROUTES_SAVE = "/routes/save";
    // 路由-删除
    String ROUTES_DELETE = "/routes/delete";
    // 路由-查询所有类型
    String ROUTES_TYPES = "/routes/types";

    // 应用-分页列表
    String APP_PAGES = "/app/pages";
    // 应用-新增/修改
    String APP_SAVE = "/app/save";
    // 应用-更新所拥有的接口
    String APP_SAVE_APIS = "/app/save/apis";
    // 应用-删除
    String APP_DELETE = "/app/delete";

    // 接口-分页列表
    String API_PAGES = "/api/pages";
    // 接口-新增/修改
    String API_SAVE = "/api/save";
    // 接口-删除
    String API_DELETE = "/api/delete";

    // 接口分类-列表(所有)
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

}
