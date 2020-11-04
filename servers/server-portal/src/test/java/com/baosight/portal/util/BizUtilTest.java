package com.baosight.portal.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.StringUtil;
import org.open.gateway.portal.constants.DateTimeFormatters;
import org.open.gateway.portal.utils.BizUtil;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by miko on 9/25/20.
 *
 * @author MIKO
 */
@Slf4j
public class BizUtilTest {

    @Test
    public void testEncodeAndDecodePassword() {
        String plainPassword = "admin123";
        String originalKey = LocalDateTime.now().format(DateTimeFormatters.yyyy_MM_dd_HH_mm_ss);
        log.info("originalKey is:{}", originalKey);
        String key = SecureUtil.md5(originalKey);
        String secretPassword = BizUtil.encodePassword(plainPassword, key);
        log.info("[encode] plainPassword is:{} key is:{} secretPassword is:{}", plainPassword, key, secretPassword);
        String newPlainPassword = BizUtil.decodePassword(secretPassword, key);
        log.info("[decode] secretPassword is:{} key is:{} newPlainPassword is:{}", secretPassword, key, newPlainPassword);
        Assert.isTrue(plainPassword.equals(newPlainPassword), "new plain password not equals original plain password");
    }

    @Disabled
    @Test
    public void testGenerateToken() {
        // WVdSdGFXNDBORFV6TlRJPWI5ZjllNDMzOTQ4NWM3MTU=
        // WVdSdGFXNHhNek0yTURVMk1nPT0yNTFhNWJmZjI4YzVkMGYw
        String account = "admin";
        String password = "admin123";
        Duration duration = Duration.ofMinutes(30);

//        String token = BizUtil.generateToken(account, password, duration);
//        System.out.println(token);
//        String token2 = BizUtil.generateToken(account, password, duration);
//        System.out.println(token2);

//        long testTimestamp = Dates.toTimestamp(LocalDateTime.of(2020, 10, 21, 16, 9, 59));
        long testTimestamp = duration.toMillis() * 2 * 445352 - 1;
        String token1 = BizUtil.generateToken(account, password, testTimestamp / (duration.toMillis() * 2));
        System.out.println("token1:" + token1);
        String token2 = BizUtil.generateToken(account, password, (testTimestamp + 1) / (duration.toMillis() * 2));
        System.out.println("token2:" + token2);
    }

    @Test
    @Disabled
    public void testGenerateDigestPassword() {
        // 295ea4895ff1fbaa777efc7b3df883c797eceda286894ab34b5e037cc93b5e7a
        String digestPassword = BizUtil.generateDigestPassword("admin123", "BqxqYtJulqcBHBzU");
        System.out.println("digestPassword is:" + digestPassword);
    }

    @Test
    @Disabled
    public void generateClientId() {
        String appCode = StringUtil.appendBlankByLength("T10682", 16);
        String clientId = Base64.encodeUrlSafe(appCode);
        System.out.println("clientId:" + clientId + " length:" + clientId.length() + " decoded:" + Base64.decodeStr(clientId));
        String clientId2 = Base64.encode(appCode);
        System.out.println("clientId2:" + clientId2 + " length:" + clientId2.length() + " decoded:" + Base64.decodeStr(clientId));
        System.out.println(BizUtil.generateClientSecret());
//        int length = -1;
//        for (int i = 0; i < 10000; i++) {
//            String appCode = StringUtil.appendBlankByLength(StringUtil.randomString(5), 8);
//            String c = SecureUtil.des(key.getBytes()).encryptHex(appCode) + StringUtil.randomString(8);
//            if (length == -1) {
//                length = c.length();
//                System.out.println("length:" + length);
//            } else {
//                Assert.isTrue(length == c.length(), "length must equals");
//            }
//        }
    }

}
