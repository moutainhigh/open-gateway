package org.open.gateway.portal.mq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.constants.MqConstants;
import org.open.gateway.base.entity.AccessLogs;
import org.open.gateway.portal.modules.monitor.service.GatewayAccessLogsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

import java.util.List;

/**
 * Created by miko on 2020/7/21.
 *
 * @author MIKO
 */
@Slf4j
@AllArgsConstructor
public class AccessLogListener {

    private final GatewayAccessLogsService accessLogsService;

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    key = MqConstants.ROUTING_KEY_GATEWAY_ACCESS_LOGS,
                    value = @Queue(value = MqConstants.QUEUE_GATEWAY_ACCESS_LOGS),
                    exchange = @Exchange(value = MqConstants.EXCHANGE_OPEN_GATEWAY_DIRECT, type = ExchangeTypes.DIRECT)
            ),
            containerFactory = "batchQueueRabbitListenerContainerFactory"
    )
    public void onAccess(List<AccessLogs> msg) {
        log.info("Received access log num: {}", msg.size());
        try {
            // 存入数据库
            this.accessLogsService.saveAccessLogs(msg);
        } catch (RuntimeException e) {
            log.error("Save access log failed:{}", e.getMessage());
        }
    }

}
