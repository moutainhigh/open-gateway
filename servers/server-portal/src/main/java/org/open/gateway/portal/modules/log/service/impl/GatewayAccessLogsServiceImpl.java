package org.open.gateway.portal.modules.log.service.impl;

import lombok.AllArgsConstructor;
import org.open.gateway.base.entity.AccessLogs;
import org.open.gateway.portal.modules.log.service.GatewayAccessLogsService;
import org.open.gateway.portal.persistence.mapper.GatewayAccessLogsMapperExt;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;
import org.open.gateway.portal.utils.Beans;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
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
        Assert.notNull(logs, "access logs is null");
        List<GatewayAccessLogs> bean = logs.stream()
                .map(this::toGatewayAccessLogs)
                .collect(Collectors.toList());
        BizUtil.checkUpdate(gatewayAccessLogsMapperExt.insertBatch(bean), bean.size());
    }

    private GatewayAccessLogs toGatewayAccessLogs(AccessLogs accessLogs) {
        GatewayAccessLogs bean = new GatewayAccessLogs();
        Beans.from(accessLogs).copy(bean);
        bean.setCreateTime(new Date());
        bean.setCreatePerson("mq");
        return bean;
    }

}
