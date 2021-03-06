package org.open.gateway.portal.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import org.open.gateway.common.utils.StringUtil;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
public class BizUtil {

    /**
     * 校验更新/插入
     *
     * @param count         更新计数
     * @param expectedCount 期望的计数
     */
    public static void checkUpdate(int count, int expectedCount) {
        if (count < expectedCount) {
            throw new IllegalStateException("invalid update count:" + count + " expected is:" + expectedCount);
        }
    }

    /**
     * 校验更新/插入
     *
     * @param count 更新计数
     */
    public static void checkUpdate(int count) {
        checkUpdate(count, 1);
    }

    /**
     * 生成摘要加密密码
     *
     * @param plainPassword 明文密码
     * @param salt          摘要加密盐
     * @return 密文
     */
    public static String generateDigestPassword(String plainPassword, String salt) {
        Digester digester = SecureUtil.sha256();
        return digester.digestHex(digester.digestHex(plainPassword) + salt);
    }

    /**
     * 对称加密明文秘密
     *
     * @param plainText 明文秘密
     * @param key       密钥
     * @return 密文密码
     */
    public static String encodePassword(String plainText, String key) {
        return SecureUtil.des(key.getBytes()).encryptHex(plainText);
    }

    /**
     * 解密密文密码
     *
     * @param secretText 密文
     * @param key        密钥
     * @return 明文秘密
     */
    public static String decodePassword(String secretText, String key) {
        return SecureUtil.des(key.getBytes()).decryptStr(secretText);
    }

    /**
     * 生成登录token
     *
     * @param account  帐户
     * @param password 密码
     * @param duration token持续时间
     * @return 登录token
     */
    public static String generateToken(String account, String password, Duration duration) {
        long seed = System.currentTimeMillis() / duration.toMillis();
        return generateToken(account, password, seed);
    }

    /**
     * 生成登录token
     *
     * @param account  帐户
     * @param password 密码
     * @param seed     随机种子
     * @return 登录token
     */
    public static String generateToken(String account, String password, long seed) {
        // base64(base64(帐户+随机种子) + des(sha256(密码 + 随机种子))(帐户))
        return Base64.encode(Base64.encode(account + seed) + SecureUtil.des(SecureUtil.sha256().digest(password + seed)).encryptHex(account));
    }

    /**
     * 生成客户端id
     *
     * @return 客户端id
     */
    public static String generateClientId() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 生成客户端密钥
     *
     * @return 客户端密钥
     */
    public static String generateClientSecret() {
        return StringUtil.randomString(48);
    }

    /**
     * 切分数组字符串
     *
     * @param arrayStr 数组字符串
     * @return 数组
     */
    public static List<String> splitArrayString(String arrayStr) {
        if (arrayStr == null) {
            return null;
        }
        return Arrays.asList(arrayStr.split(","));
    }

    /**
     * 拼接数组为字符串
     *
     * @param array 数组
     * @return 数组字符串
     */
    public static String joinStringArray(Iterable<String> array) {
        if (array == null) {
            return null;
        }
        return String.join(",", array);
    }

}
