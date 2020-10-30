package com.baosight.portal.util;

import com.baosight.portal.BaseTest;
import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.StringUtil;
import org.springframework.util.Assert;

/**
 * Created by miko on 10/30/20.
 *
 * @author MIKO
 */
public class StringUtilTest extends BaseTest {

    @Test
    public void randomLetter() {
        String randomLetter = StringUtil.randomLetter(5000);
        System.out.println(randomLetter);
        Assert.isTrue(randomLetter.contains("a"), "must constants a");
        Assert.isTrue(randomLetter.contains("Z"), "must constants Z");
        for (int i = 0; i < 100000; i++) {
            String str = StringUtil.randomLetter(10);
            Assert.isTrue(str.matches("^[a-zA-Z]+$"), "match fielded str:" + str);
        }
    }

    @Test
    public void randomString() {
        String randomString = StringUtil.randomString(5000);
        System.out.println(randomString);
        Assert.isTrue(randomString.contains("a"), "must constants a");
        Assert.isTrue(randomString.contains("9"), "must constants 9");
        for (int i = 0; i < 100000; i++) {
            String str = StringUtil.randomString(10);
            Assert.isTrue(str.matches("^[a-zA-Z0-9]+$"), "match fielded str:" + str);
        }
    }

}
