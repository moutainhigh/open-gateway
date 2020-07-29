package org.open.gateway.portal.service;

import open.gateway.common.base.entity.AccessLogs;

import java.util.List;

/**
 * Created by miko on 2020/7/22.
 *
 * @author MIKO
 */
public interface GatewayAccessLogsService {

    /**
     * 保存请求日志
     *
     * @param log 日志
     */
    void saveAccessLogs(List<AccessLogs> log);

}
