package org.open.gateway.base.constants;

/**
 * Created by miko on 2020/6/10.
 * 网关常量
 *
 * @author MIKO
 */
public interface GatewayConstants {

    /**
     * redis存储的key
     */
    interface RedisKey {

        // token存放redis的key的点缀
        String PREFIX_ACCESS_TOKENS = "access_token:";
        // 授权码前缀
        String PREFIX_AUTHORIZE_CODE = "authorize_code:";
        // 申请token锁前缀
        String PREFIX_LOCK = "lock:";

    }

    /**
     * 限流策略
     */
    interface RateLimitPolicy {

        // 策略类型-根据url限制访问次数
        String POLICY_TYPE_URL = "url";
        // 策略类型-根据用户限制访问次数
        String POLICY_TYPE_USER = "user";
        // 策略类型-根据url和用户限制访问次数
        String POLICY_TYPE_URL_USER = "url_user";
        // 策略类型-根据ip限制访问次数
        String POLICY_TYPE_IP = "ip";

        // 限流范围-地址
        String KEY_RESOLVER_URL = "urlKeyResolver";
        // 限流范围-地址&用户
        String KEY_RESOLVER_URL_USER = "urlUserKeyResolver";
        // 限流范围-用户
        String KEY_RESOLVER_USER = "userKeyResolver";
        // 限流范围-ip
        String KEY_RESOLVER_IP = "ipKeyResolver";

        // 单位时间-秒
        String INTERVAL_UNIT_SECONDS = "seconds";
        // 单位时间-分
        String INTERVAL_UNIT_MINUTES = "minutes";
        // 单位时间-时
        String INTERVAL_UNIT_HOURS = "hours";
        // 单位时间-天
        String INTERVAL_UNIT_DAYS = "days";

    }

    /**
     * 黑白名单策略
     */
    interface IpLimitPolicy {

        // 策略类型-拒绝/黑名单
        String POLICY_TYPE_BLACK = "black";
        // 策略类型-允许/白名单
        String POLICY_TYPE_WHITE = "white";

    }

}
