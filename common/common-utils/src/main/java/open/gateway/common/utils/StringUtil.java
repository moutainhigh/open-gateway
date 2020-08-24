package open.gateway.common.utils;

import java.util.Random;

/**
 * Created by miko on 2020/7/21.
 *
 * @author MIKO
 */
public class StringUtil {

    // 随机字母种子
    private static final char[] RANDOM_SEEDS_LETTER = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 生成随机英文字母字符串
     *
     * @param len 字符串长度
     * @return 随机字符串
     */
    public static String randomLetter(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(RANDOM_SEEDS_LETTER[random.nextInt(RANDOM_SEEDS_LETTER.length)]);
        }
        return sb.toString();
    }

    /**
     * 生成随机英文字母字符串
     *
     * @param len 字符串长度
     * @return 随机字符串
     */
    public static String randomNumber(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

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
