package org.open.gateway.test;

import org.junit.jupiter.api.Test;
import org.open.gateway.common.utils.ip.IpRange;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpRangeTest {

    @Test
    public void testEmpty() {
        IpRange ipRange = new IpRange("127.0.0.1");
        System.out.println("from:" + ipRange.getFrom());
        System.out.println("to:" + ipRange.getTo());
    }

    @Test
    public void testPattern1() {
        IpRange ipRange = new IpRange("255.255.255.74/24");
        IpRange ipRange2 = new IpRange("255.255.255.74/23");
        IpRange ipRange3 = new IpRange("255.255.255.74/22");
        IpRange ipRange4 = new IpRange("1.255.32.4/24");
        IpRange ipRange5 = new IpRange("0.0.0.255/25");

        assertEquals(0xFFFFFF00L, ipRange.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange.getTo());
        assertEquals(0xFFFFFE00L, ipRange2.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange2.getTo());
        assertEquals(0xFFFFFC00L, ipRange3.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange3.getTo());
        assertEquals(0x01FF2000L, ipRange4.getFrom());
        assertEquals(0x01FF20FFL, ipRange4.getTo());
        assertEquals(0x00000080L, ipRange5.getFrom());
        assertEquals(0x000000FFL, ipRange5.getTo());
    }

    @Test
    public void testPattern2() {
        IpRange ipRange = new IpRange("255.255.255.*");
        IpRange ipRange2 = new IpRange("255.255.*.*");
        IpRange ipRange3 = new IpRange("1.0.255.*");
        IpRange ipRange4 = new IpRange("255.*.*.*");

        assertEquals(0xFFFFFF00L, ipRange.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange.getTo());
        assertEquals(0xFFFF0000L, ipRange2.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange2.getTo());
        assertEquals(0x0100FF00L, ipRange3.getFrom());
        assertEquals(0x0100FFFFL, ipRange3.getTo());
        assertEquals(0xFF000000L, ipRange4.getFrom());
        assertEquals(0xFFFFFFFFL, ipRange4.getTo());
    }

    @Test
    public void testPattern3() {
        IpRange ipRange = new IpRange("0.0.0.32");
        IpRange ipRange2 = new IpRange("0.0.1.32");
        IpRange ipRange3 = new IpRange("0.0.255.32");
        IpRange ipRange4 = new IpRange("128.0.0.1");

        assertEquals(0x00000020L, ipRange.getFrom());
        assertEquals(0x00000020L, ipRange.getTo());
        assertEquals(288L, ipRange2.getFrom());
        assertEquals(288L, ipRange2.getTo());
        assertEquals(65312L, ipRange3.getFrom());
        assertEquals(65312L, ipRange3.getTo());
        assertEquals(0x80000001L, ipRange4.getFrom());
        assertEquals(0x80000001L, ipRange4.getTo());
    }
}