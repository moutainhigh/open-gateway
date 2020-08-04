package open.gateway.common.base.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import open.gateway.common.base.constants.GatewayConstants;

import java.util.*;

@Getter
@ToString
public class GatewayIpLimitDefinition {

    /**
     * 黑名单
     */
    private final List<IpLimit> blackList = new ArrayList<>();

    /**
     * 白名单
     */
    private final List<IpLimit> whiteList = new ArrayList<>();

    public GatewayIpLimitDefinition(Collection<IpLimit> ipLimits) {
        Objects.requireNonNull(ipLimits);
        for (IpLimit ipLimit : ipLimits) {
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_BLACK.equals(ipLimit.policyType)) {
                this.blackList.add(ipLimit);
            }
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_WHITE.equals(ipLimit.policyType)) {
                this.whiteList.add(ipLimit);
            }
        }
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
        if (this.blackList.stream().anyMatch(black -> black.isAccessDenied(ip))) {
            return false;
        }
        // 白名单校验
        if (this.whiteList.stream().anyMatch(white -> white.isAccessDenied(ip))) {
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

        /**
         * 是否拒绝访问
         *
         * @param ip ip地址
         * @return true拒绝，false允许
         */
        public boolean isAccessDenied(String ip) {
            Objects.requireNonNull(this.policyType);
            Objects.requireNonNull(this.ipAddresses);
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_BLACK.equals(this.policyType)) {
                // 在黑名单中拒绝访问
                return this.ipAddresses.contains(ip);
            }
            if (GatewayConstants.IpLimitPolicy.POLICY_TYPE_WHITE.equals(this.policyType)) {
                // 不在白名单中拒绝访问
                return !this.ipAddresses.contains(ip);
            }
            throw new IllegalStateException("Invalid policy type:" + this.policyType);
        }

    }

}