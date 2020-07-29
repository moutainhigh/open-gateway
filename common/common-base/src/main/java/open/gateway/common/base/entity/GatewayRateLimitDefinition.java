package open.gateway.common.base.entity;

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

    // 单位时间秒
    /**
     * 1分钟
     */
    public static final long SECONDS_IN_MINUTE = 60;
    /**
     * 1小时
     */
    public static final long SECONDS_IN_HOUR = 3600;
    /**
     * 1天
     */
    public static final long SECONDS_IN_DAY = 24 * 3600;

    /**
     * 获取单位时间内刷新时长
     */
    public long getInterval() {
        String timeUnit = this.intervalUnit;
        if (timeUnit.equalsIgnoreCase(TimeUnit.SECONDS.name())) {
            return SECONDS_IN_MINUTE;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.MINUTES.name())) {
            return SECONDS_IN_MINUTE;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.HOURS.name())) {
            return SECONDS_IN_HOUR;
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.DAYS.name())) {
            return SECONDS_IN_DAY;
        } else {
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

}