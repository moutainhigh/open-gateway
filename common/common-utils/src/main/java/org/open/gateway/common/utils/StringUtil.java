package org.open.gateway.common.utils;

import java.util.Random;

/**
 * Created by miko on 2020/7/21.
 * 字符串工具类
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
     * 是否有内容
     *
     * @param str 字符串
     * @return true有内容, false没有内容
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 是否为空
     *
     * @param str 字符串
     * @return true为空, false不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 计算字符串{@code str}中子字符串{@code sub}的出现次数
     *
     * @param str 字符串
     * @param sub 子字符串
     * @return 出现次数
     */
    public static int countOccurrencesOf(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }

        int count = 0;
        int pos = 0;
        int idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

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
    public static String splitByLenLimit(String str, int maxLen) {
        if (str.length() > maxLen) {
            return str.substring(0, maxLen);
        }
        return str;
    }

}
