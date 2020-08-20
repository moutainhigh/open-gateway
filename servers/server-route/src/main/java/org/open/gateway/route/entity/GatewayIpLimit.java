package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.GatewayConstants;
import org.open.gateway.route.utils.ip.IpMatcher;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
@ToString
public class GatewayIpLimit {

    /**
     * 黑名单过滤器
     */
    private final IpMatcher blackMatcher;
    /**
     * 白名单过滤器
     */
    private final IpMatcher whiteMatcher;

    public GatewayIpLimit(Collection<IpLimit> ipLimits) {
        Objects.requireNonNull(ipLimits);
        Set<String> blackIps = new HashSet<>();
        Set<String> whiteIps = new HashSet<>();
        for (IpLimit ipLimit : ipLimits) {
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_BLACK.equals(ipLimit.policyType)) {
                for (String ipAddress : ipLimit.ipAddresses) {
                    try {
                        blackIps.add(InetAddress.getByName(ipAddress).getHostAddress());
                    } catch (UnknownHostException e) {
                        log.error("Invalid ip address:" + ipAddress, e);
                    }
                }
            }
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_WHITE.equals(ipLimit.policyType)) {
                for (String ipAddress : ipLimit.ipAddresses) {
                    try {
                        blackIps.add(InetAddress.getByName(ipAddress).getHostAddress());
                    } catch (UnknownHostException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        this.blackMatcher = new IpMatcher(blackIps);
        this.whiteMatcher = new IpMatcher(whiteIps);
    }

    /**
     * 是否允许访问
     *
     * @param ip ip地址
     * @return true允许，false不允许
     */
    @SuppressWarnings("RedundantIfStatement")
    public boolean isAccessAllowed(String ip) {
        // 黑名单校验
        if (!CollectionUtils.isEmpty(this.blackMatcher.getRequiredIps()) && this.blackMatcher.matches(ip)) {
            return false;
        }
        // 白名单校验
        if (!CollectionUtils.isEmpty(this.whiteMatcher.getRequiredIps()) && this.whiteMatcher.matches(ip)) {
            return false;
        }
        return true;
    }

    @Getter
    @Setter
    @ToString
    public static class IpLimit {

        /**
         * api编码
         */
        private String apiCode;

        /**
         * 黑白名单策略类型
         */
        private String policyType;

        /**
         * ip地址
         */
        private Set<String> ipAddresses;

    }

}