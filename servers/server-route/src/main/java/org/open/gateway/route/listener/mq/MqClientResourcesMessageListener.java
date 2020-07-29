package org.open.gateway.route.listener.mq;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.MqConstants;
import open.gateway.common.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableClientResourcesRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
@Slf4j
public class MqClientResourcesMessageListener {

    private final RefreshableClientResourcesRepository clientResourcesRepository;

    public MqClientResourcesMessageListener(RefreshableClientResourcesRepository clientResourcesRepository) {
        this.clientResourcesRepository = clientResourcesRepository;
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(), //注意这里不要定义队列名称,系统会随机产生
                    exchange = @Exchange(value = MqConstants.EXCHANGE_REFRESH_CLIENT_RESOURCES, type = ExchangeTypes.FANOUT)
            )
    )
    public void onRefreshRoutes(RefreshGateway msg) {
        log.info("MQ[{}] listener received msg: {}", MqConstants.EXCHANGE_REFRESH_CLIENT_RESOURCES, msg);
        this.clientResourcesRepository
                .refresh(msg)
                .subscribe();
    }

}
