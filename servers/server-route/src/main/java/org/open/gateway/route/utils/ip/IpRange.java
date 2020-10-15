package org.open.gateway.route.utils.ip;

import org.springframework.util.StringUtils;

/**
 * @author miko
 */
public class IpRange implements Comparable<IpRange> {

    private static final int IPV4_BIT_COUNT = 32;
    private long from;
    private long to;

    public IpRange(String ipPattern) {
        convert(ipPattern);
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public void setTo(long to) {
        this.to = to;
    }

    private void convert(String ipPattern) {
        if (StringUtils.isEmpty(ipPattern)) {
            throw new IllegalArgumentException("IpPattern cannot be null");
        }

        String ip;
        long maskBitCount;
        if (ipPattern.indexOf("/") > 0) {
            String[] addressAndMask = ipPattern.split("/");
            ip = addressAndMask[0];
            maskBitCount = Long.parseLong(addressAndMask[1]);
        } else if (ipPattern.indexOf('*') > 0) {
            ip = ipPattern.replaceAll("\\*", "255");
            maskBitCount = IPV4_BIT_COUNT - ((long) StringUtils.countOccurrencesOf(ipPattern, "*") * 8);
        } else {
            ip = ipPattern;
            maskBitCount = IPV4_BIT_COUNT;
        }

        String[] splitIps = ip.split("\\.");
        for (String splitIp : splitIps) {
            from = from << 8;
            from |= Long.parseLong(splitIp);
        }

        to = from;
        long mask = 0xFFFFFFFFL >>> maskBitCount;

        to = to | mask;
        from = from & (~mask);

    }

    @Override
    public int compareTo(IpRange o) {
        if (this.getFrom() != o.getFrom()) {
            return this.getFrom() > o.getFrom() ? 1 : -1;
        }
        if (this.getTo() != o.getTo()) {
            return this.getTo() > o.getTo() ? 1 : -1;
        }
        return 0;
    }

}
