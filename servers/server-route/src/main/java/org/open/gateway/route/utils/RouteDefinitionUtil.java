package org.open.gateway.route.utils;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by miko on 2020/7/17.
 * RouteDefinition工具类. 注:线程不安全
 *
 * @author MIKO
 */
public class RouteDefinitionUtil {

    /**
     * api编码
     */
    private static final String METADATA_KEY_API_CODE = "api_code";
    /**
     * 分组代码
     */
    private static final String METADATA_KEY_GROUP_CODES = "group_codes";
    /**
     * 路由编码
     */
    private static final String METADATA_KEY_ROUTE_CODE = "route_code";
    /**
     * 是否需要认证
     */
    private static final String METADATA_KEY_IS_AUTH = "is_auth";

    public static String getApiCode(RouteDefinition routeDefinition) {
        return getApiCode(routeDefinition.getMetadata());
    }

    public static String getApiCode(Map<String, Object> metadata) {
        return (String) metadata.get(METADATA_KEY_API_CODE);
    }

    public static void setApiCode(RouteDefinition routeDefinition, String apiCode) {
        putMetadata(routeDefinition, METADATA_KEY_API_CODE, apiCode);
    }

    public static Set<String> getGroupCodes(RouteDefinition routeDefinition) {
        return getGroupCodes(routeDefinition.getMetadata());
    }


    @SuppressWarnings("unchecked")
    public static Set<String> getGroupCodes(Map<String, Object> metadata) {
        return (Set<String>) metadata.get(METADATA_KEY_GROUP_CODES);
    }

    public static void setGroupCodes(RouteDefinition routeDefinition, Set<String> groupCodes) {
        putMetadata(routeDefinition, METADATA_KEY_GROUP_CODES, groupCodes);
    }

    public static String getRouteCode(RouteDefinition routeDefinition) {
        return getRouteCode(routeDefinition.getMetadata());
    }

    public static String getRouteCode(Map<String, Object> metadata) {
        return (String) metadata.get(METADATA_KEY_ROUTE_CODE);
    }

    public static void setRouteCode(RouteDefinition routeDefinition, String routeCode) {
        putMetadata(routeDefinition, METADATA_KEY_ROUTE_CODE, routeCode);
    }

    public static boolean getIsAuth(RouteDefinition routeDefinition) {
        return getIsAuth(routeDefinition.getMetadata());
    }

    public static boolean getIsAuth(Map<String, Object> metadata) {
        return (boolean) metadata.get(METADATA_KEY_IS_AUTH);
    }

    public static void setIsAuth(RouteDefinition routeDefinition, boolean isOpen) {
        putMetadata(routeDefinition, METADATA_KEY_IS_AUTH, isOpen);
    }

    private static void putMetadata(RouteDefinition routeDefinition, String key, Object arg) {
        if (routeDefinition.getMetadata() == null) {
            Map<String, Object> metadata = new HashMap<>(4);
            metadata.put(key, arg);
            routeDefinition.setMetadata(metadata);
        } else {
            routeDefinition.getMetadata().put(key, arg);
        }
    }

}
