package org.open.gateway.route.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
public class GatewayRateLimitDefinition {

    /**
     * 限流策略id
     */
    private Long id;

    /**
     * 策略类型 all 限制任何人访问api, user 限制指定用户访问api
     */
    private String policyType;

    /**
     * 平均限流次数
     */
    private Long limitQuota;

    /**
     * 一秒内允许的最大限流次数
     */
    private Long maxLimitQuota;

    /**
     * 单位时间 seconds-秒,minutes-分钟,hours-小时,days-天
     */
    private String intervalUnit;

    /**
     * 获取每次请求耗费token的数量
     */
    public long requestedTokens() {
        String timeUnit = this.intervalUnit;
        if (timeUnit.equalsIgnoreCase(TimeUnit.SECONDS.name())) {
            return 1;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.MINUTES.name())) {
            return 60;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.HOURS.name())) {
            return 60 * 60;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.DAYS.name())) {
            return 60 * 60 * 24;
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

}