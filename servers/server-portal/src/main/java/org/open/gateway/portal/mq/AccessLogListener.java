package org.open.gateway.portal.mq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.MqConstants;
import open.gateway.common.base.entity.AccessLogs;
import open.gateway.common.utils.JSON;
import org.open.gateway.portal.service.GatewayAccessLogsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

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
                    value = @Queue(value = MqConstants.QUEUE_GATEWAY_ACCESS_LOGS), //注意这里不要定义队列名称,系统会随机产生
                    exchange = @Exchange(value = MqConstants.EXCHANGE_GATEWAY_ACCESS_LOGS, type = ExchangeTypes.DIRECT)
            )
    )
    public void onAccess(AccessLogs msg) {
        log.info("Received access log is:{}", JSON.toJSONString(msg));
        try {
            // 存入数据库
            this.accessLogsService.saveAccessLogs(msg);
            log.info("Save access log finished");
        } catch (RuntimeException e) {
            log.error("Save access log failed:{}", e.getMessage());
        }
    }

}
