package org.open.gateway.portal.utils;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
public class BizUtil {


    /**
     * 校验更新/插入
     *
     * @param count 更新计数
     */
    public static void checkUpdate(int count) {
        if (count < 1) {
            throw new IllegalStateException("invalid update count");
        }
    }

}
