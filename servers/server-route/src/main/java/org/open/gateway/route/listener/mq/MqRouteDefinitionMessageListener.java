package org.open.gateway.route.listener.mq;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.MqConstants;
import open.gateway.common.base.entity.RefreshGateway;
import org.open.gateway.route.repositories.RefreshableRouteDefinitionRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * Created by miko on 2020/7/14.
 *
 * @author MIKO
 */
@Slf4j
public class MqRouteDefinitionMessageListener implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private final RefreshableRouteDefinitionRepository routeDefinitionRepository;

    public MqRouteDefinitionMessageListener(RefreshableRouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
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
                .doOnSuccess(sub -> this.refreshRoutes())
                .subscribe();
    }

    /**
     * 刷新路由
     */
    private void refreshRoutes() {
        // 发布刷新路由事件
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        log.info("Publish refresh routes event finished");
    }

}
