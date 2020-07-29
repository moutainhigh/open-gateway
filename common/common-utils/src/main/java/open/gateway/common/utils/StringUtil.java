package open.gateway.common.utils;

/**
 * Created by miko on 2020/7/21.
 *
 * @author MIKO
 */
public class StringUtil {

    /**
     * 获取字符串，超出最大长度的部分被舍弃
     *
     * @param str    源字符串
     * @param maxLen 限定的最大长度
     * @return 处理后的字符串
     */
    public static String trimByLenLimit(String str, int maxLen) {
        if (str.length() > maxLen) {
            return str.substring(0, maxLen);
        }
        return str;
    }

}
