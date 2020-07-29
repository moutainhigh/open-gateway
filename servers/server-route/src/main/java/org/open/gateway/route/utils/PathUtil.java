package org.open.gateway.route.utils;

/**
 * Created by miko on 2020/7/10.
 *
 * @author MIKO
 */
public class PathUtil {

    /**
     * 获取路由全路径
     *
     * @param routePath 路由路径
     * @param apiPath   api路径
     * @return 路由全路径
     */
    public static String getFullPath(String routePath, String apiPath) {
        if (apiPath == null) {
            return routePath;
        }
        String formatApiUrl = apiPath.startsWith("/") ? apiPath : "/" + apiPath;
        if (routePath != null) {
            return routePath + formatApiUrl;
        }
        return formatApiUrl;
    }

}
