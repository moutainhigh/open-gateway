package org.open.gateway.portal.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;

import java.time.Duration;

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
     * 转换密码
     *
     * @param plainPassword 明文密码
     * @param salt          摘要加密盐
     * @return 密文
     */
    public static String getSecretPassword(String plainPassword, String salt) {
        Digester digester = SecureUtil.sha256();
        return digester.digestHex(digester.digestHex(plainPassword) + digester.digestHex(salt));
    }

    /**
     * 加密明文秘密
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

}
