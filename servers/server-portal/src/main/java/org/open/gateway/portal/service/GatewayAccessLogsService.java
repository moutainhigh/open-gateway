package org.open.gateway.portal.service;

import open.gateway.common.base.entity.AccessLogs;

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
    void saveAccessLogs(AccessLogs log);

}
