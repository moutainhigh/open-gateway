package org.open.gateway.route.listener.mq;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
@Slf4j
public class MqRouteDefinitionMessageListener {

    private final RefreshableRouteDefinitionRepository routeDefinitionRepository;

    public MqRouteDefinitionMessageListener(RefreshableRouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(), //注意这里不要定义队列名称,系统会随机产生
                    exchange = @Exchange(value = MqConstants.EXCHANGE_REFRESH_ROUTES, type = ExchangeTypes.FANOUT)
            )
    )
    public void onRefreshRoutes(RefreshGateway msg) {
        log.info("MQ[{}] listener received msg: {}", MqConstants.EXCHANGE_REFRESH_ROUTES, msg);
        this.routeDefinitionRepository
                .refresh(msg)
                .subscribe();
    }

}
