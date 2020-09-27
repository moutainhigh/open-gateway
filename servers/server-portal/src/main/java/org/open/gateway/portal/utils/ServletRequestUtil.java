package org.open.gateway.portal.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by miko on 9/27/20.
 *
 * @author MIKO
 */
public class ServletRequestUtil {

    /**
     * 请求头中的ip字段
     */
    private static final String[] IP_HEADERS = new String[]{"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "X-Real-IP"};

    /**
     * 从请求信息中获取ip
     *
     * @param request 请求信息
     * @return ip地址
     */
    public static String getIpFromRequest(HttpServletRequest request) {
        for (String key : IP_HEADERS) {
            String ip = request.getHeader(key);
            if (ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        if (request.getRemoteAddr() != null) {
            return request.getRemoteAddr();
        }
        return null;
    }

}
