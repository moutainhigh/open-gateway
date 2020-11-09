package org.open.gateway.route.listener.mq;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableTokenRepository;
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
public class RefreshTokenMessageListener {

    private final RefreshableTokenRepository repository;

    public RefreshTokenMessageListener(RefreshableTokenRepository refreshableTokenRepository) {
        this.repository = refreshableTokenRepository;
    }

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    key = MqConstants.ROUTING_KEY_REFRESH_CLIENT_TOKEN,
                    value = @Queue(MqConstants.QUEUE_REFRESH_CLIENT_TOKEN),
                    exchange = @Exchange(value = MqConstants.EXCHANGE_OPEN_GATEWAY_DIRECT, type = ExchangeTypes.DIRECT)
            )
    )
    public void onRefreshRoutes(RefreshGateway msg) {
        log.info("MQ[{}] listener received msg: {}", MqConstants.QUEUE_REFRESH_CLIENT_TOKEN, msg);
        this.repository
                .refresh(msg)
                .subscribe();
    }

}
