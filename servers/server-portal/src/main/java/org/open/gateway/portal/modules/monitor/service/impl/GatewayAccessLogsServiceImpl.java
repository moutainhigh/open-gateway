package org.open.gateway.portal.modules.monitor.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.base.entity.AccessLogs;
import org.open.gateway.portal.modules.monitor.service.GatewayAccessLogsService;
import org.open.gateway.portal.persistence.mapper.GatewayAccessLogsMapperExt;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;
import org.open.gateway.portal.utils.Beans;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Slf4j
@Service
@AllArgsConstructor
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {

    private final GatewayAccessLogsMapperExt gatewayAccessLogsMapperExt;

    /**
     * 保存请求日志
     *
     * @param logs 日志
     */
    @Override
    public void saveAccessLogs(List<AccessLogs> logs) {
        log.info("Starting save access log. data num: {}", logs.size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<GatewayAccessLogs> bean = logs.stream()
                .map(this::toGatewayAccessLogs)
                .collect(Collectors.toList());
        BizUtil.checkUpdate(gatewayAccessLogsMapperExt.insertBatch(bean), bean.size());
        stopWatch.stop();
        log.info("Save access log finished end: {} ms", stopWatch.getTotalTimeMillis());
    }

    private GatewayAccessLogs toGatewayAccessLogs(AccessLogs accessLogs) {
        GatewayAccessLogs bean = new GatewayAccessLogs();
        Beans.from(accessLogs).copy(bean);
        bean.setCreateTime(new Date());
        bean.setCreatePerson("mq");
        return bean;
    }

}
