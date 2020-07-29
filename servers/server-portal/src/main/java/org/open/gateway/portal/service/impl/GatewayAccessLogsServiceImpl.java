package org.open.gateway.portal.service.impl;

import lombok.AllArgsConstructor;
import open.gateway.common.base.entity.AccessLogs;
import org.open.gateway.portal.persistence.mapper.GatewayAccessLogsMapper;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;
import org.open.gateway.portal.service.GatewayAccessLogsService;
import org.open.gateway.portal.utils.Beans;
import org.open.gateway.portal.utils.BizUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Service
@AllArgsConstructor
public class GatewayAccessLogsServiceImpl implements GatewayAccessLogsService {

    private final GatewayAccessLogsMapper gatewayAccessLogsMapper;

    /**
     * 保存请求日志
     *
     * @param log 日志
     */
    @Override
    public void saveAccessLogs(AccessLogs log) {
        GatewayAccessLogs bean = toGatewayAccessLogs(log);
        Assert.notNull(log, "access logs is null");
        BizUtil.checkUpdate(gatewayAccessLogsMapper.insertSelective(bean));
    }

    private GatewayAccessLogs toGatewayAccessLogs(AccessLogs accessLogs) {
        GatewayAccessLogs bean = new GatewayAccessLogs();
        Beans.from(accessLogs).copy(bean);
        bean.setCreateTime(new Date());
        bean.setCreatePerson("mq");
        return bean;
    }

}
