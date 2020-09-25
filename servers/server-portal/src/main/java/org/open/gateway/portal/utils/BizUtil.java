package org.open.gateway.portal.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;

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
     * 生成token
     *
     * @param account  帐户
     * @param password 密码
     * @return 登录token
     */
    public static String generateToken(String account, String password) {
        return SecureUtil.des(SecureUtil.sha256().digest(password)).encryptHex(account);
    }

}
