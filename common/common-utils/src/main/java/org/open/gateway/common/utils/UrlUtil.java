package org.open.gateway.common.utils;

/**
 * Created by miko on 2020/7/10.
 * url工具类
 *
 * @author MIKO
 */
public class UrlUtil {

    /**
     * 拼接url路径
     *
     * @param url   原始路径
     * @param paths 子路径
     * @return 路由全路径
     */
    public static String appendUrlPath(String url, String... paths) {
        if (paths == null || paths.length == 0) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        for (String path : paths) {
            if (!path.startsWith("/")) {
                sb.append("/");
            }
            sb.append(path);
        }
        return sb.toString();
    }

    /**
     * 清理请求url参数
     *
     * @param url 原始请求路径
     * @return 清理后的请求路径
     */
    public static String trimUrlParameter(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

}
