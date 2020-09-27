package com.baosight.portal.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.open.gateway.portal.utils.BizUtil;
import org.open.gateway.portal.constants.DateTimeFormatters;

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

}
