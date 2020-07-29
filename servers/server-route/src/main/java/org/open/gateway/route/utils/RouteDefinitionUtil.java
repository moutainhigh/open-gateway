package org.open.gateway.route.utils;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by miko on 2020/7/17.
 *
 * @author MIKO
 */
public class RouteDefinitionUtil {

    private static final String METADATA_KEY_API_CODE = "api_code";
    private static final String METADATA_KEY_ROUTE_CODE = "route_code";
    private static final String METADATA_KEY_IS_AUTH = "is_auth";
    private static final String METADATA_KEY_IS_OPEN = "is_open";

    public static String getApiCode(RouteDefinition routeDefinition) {
        return getApiCode(routeDefinition.getMetadata());
    }

    public static String getApiCode(Map<String, Object> metadata) {
        return (String) metadata.get(METADATA_KEY_API_CODE);
    }

    public static void setApiCode(RouteDefinition routeDefinition, String apiCode) {
        putMetadata(METADATA_KEY_API_CODE, routeDefinition, apiCode);
    }

    public static String getRouteCode(RouteDefinition routeDefinition) {
        return getRouteCode(routeDefinition.getMetadata());
    }

    public static String getRouteCode(Map<String, Object> metadata) {
        return (String) metadata.get(METADATA_KEY_ROUTE_CODE);
    }

    public static void setRouteCode(RouteDefinition routeDefinition, String routeCode) {
        putMetadata(METADATA_KEY_ROUTE_CODE, routeDefinition, routeCode);
    }

    public static boolean getIsAuth(RouteDefinition routeDefinition) {
        return getIsAuth(routeDefinition.getMetadata());
    }

    public static boolean getIsAuth(Map<String, Object> metadata) {
        return (boolean) metadata.get(METADATA_KEY_IS_AUTH);
    }

    public static void setIsAuth(RouteDefinition routeDefinition, boolean isOpen) {
        putMetadata(METADATA_KEY_IS_AUTH, routeDefinition, isOpen);
    }

    public static boolean getIsOpen(RouteDefinition routeDefinition) {
        return getIsOpen(routeDefinition.getMetadata());
    }

    public static boolean getIsOpen(Map<String, Object> metadata) {
        return (boolean) metadata.get(METADATA_KEY_IS_OPEN);
    }

    public static void setIsOpen(RouteDefinition routeDefinition, boolean isOpen) {
        putMetadata(METADATA_KEY_IS_OPEN, routeDefinition, isOpen);
    }


    private static void putMetadata(String key, RouteDefinition routeDefinition, Object arg) {
        if (routeDefinition.getMetadata() == null) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put(key, arg);
            routeDefinition.setMetadata(metadata);
        } else {
            routeDefinition.getMetadata().put(key, arg);
        }
    }

}
