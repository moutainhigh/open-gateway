package org.open.gateway.route.listener.mq;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableIpLimitRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
@Slf4j
@Component
public class IpLimitMessageListener {

    private final RefreshableIpLimitRepository ipLimitRepository;

    public IpLimitMessageListener(RefreshableIpLimitRepository ipLimitRepository) {
        this.ipLimitRepository = ipLimitRepository;
    }

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(), //注意这里不要定义队列名称,系统会随机产生
                    exchange = @Exchange(value = MqConstants.EXCHANGE_REFRESH_IP_LIMITS, type = ExchangeTypes.FANOUT)
            )
    )
    public void onRefreshRoutes(RefreshGateway msg) {
        log.info("MQ[{}] listener received msg: {}", MqConstants.EXCHANGE_REFRESH_IP_LIMITS, msg);
        this.ipLimitRepository
                .refresh(msg)
                .subscribe();
    }


}
