package open.gateway.common.base.constants;

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

        // 路由表存放redis的key
        String ROUTES = "gateway_routes";

        // token存放redis的key的点缀
        String PREFIX_ACCESS_TOKENS = "access_token:";

    }

    /**
     * 是否已删除
     */
    interface IsDel {

        // 已删除
        int YES = 1;
        // 未删除
        int NO = 0;
    }

    /**
     * 限流策略
     */
    interface RateLimitPolicy {

        // 策略类型-限制任何人访问api
        String POLICY_TYPE_ALL = "all";
        // 策略类型-限制指定用户访问api
        String POLICY_TYPE_USER = "user";
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
