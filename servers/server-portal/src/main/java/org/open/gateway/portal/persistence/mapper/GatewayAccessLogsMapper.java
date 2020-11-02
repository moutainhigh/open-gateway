package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayAccessLogs;

public interface GatewayAccessLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GatewayAccessLogs record);

    int insertSelective(GatewayAccessLogs record);

    GatewayAccessLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayAccessLogs record);

    int updateByPrimaryKey(GatewayAccessLogs record);
}