package org.open.gateway.test;

import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.ip.IpMatcher;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IpMatcherTest {

    List<String> ipList = Arrays.asList("52.34.55.*", "111.111.112.*", "123.255.255.1", "1.2.3.4", "0.2.0.2", "255.255.255.35/23");
    IpMatcher ipMatcher = new IpMatcher(ipList);

    @Test
    public void testMatch() {
        long begin = System.currentTimeMillis();
        assertTrue(ipMatcher.matches("52.34.55.123"));
        assertTrue(ipMatcher.matches("52.34.55.0"));
        assertTrue(ipMatcher.matches("52.34.55.255"));
        assertTrue(ipMatcher.matches("52.34.55.23"));
        assertFalse(ipMatcher.matches("51.34.55.20"));
        assertFalse(ipMatcher.matches("52.32.55.23"));
        assertFalse(ipMatcher.matches("52.34.53.23"));
        assertFalse(ipMatcher.matches("51.32.54.23"));

        assertTrue(ipMatcher.matches("111.111.112.0"));
        assertTrue(ipMatcher.matches("111.111.112.255"));
        assertTrue(ipMatcher.matches("111.111.112.3"));
        assertTrue(ipMatcher.matches("111.111.112.95"));
        assertFalse(ipMatcher.matches("111.111.113.95"));
        assertFalse(ipMatcher.matches("111.112.112.95"));
        assertFalse(ipMatcher.matches("101.111.112.9"));

        assertTrue(ipMatcher.matches("123.255.255.1"));
        assertFalse(ipMatcher.matches("123.255.255.2"));
        assertFalse(ipMatcher.matches("123.255.255.0"));
        assertFalse(ipMatcher.matches("123.254.255.1"));
        assertFalse(ipMatcher.matches("123.255.254.1"));
        assertFalse(ipMatcher.matches("122.255.254.1"));

        assertFalse(ipMatcher.matches("1.2.3.3"));
        assertFalse(ipMatcher.matches("1.2.3.0"));
        assertFalse(ipMatcher.matches("7.2.3.0"));
        assertFalse(ipMatcher.matches("243.2.3.1"));
        assertTrue(ipMatcher.matches("0.2.0.2"));
        assertTrue(ipMatcher.matches("1.2.3.4"));

        long end = System.currentTimeMillis();
        System.out.println("spend:" + (end - begin) + "ms.");
    }
}