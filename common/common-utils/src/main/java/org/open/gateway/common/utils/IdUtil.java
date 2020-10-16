package org.open.gateway.common.utils;

import java.util.UUID;

/**
 * Created by miko on 2020/7/8.
 * id工具类
 *
 * @author MIKO
 */
public class IdUtil {

    /**
     * 生成uuid
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
