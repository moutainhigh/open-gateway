package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.open.gateway.portal.modules.monitor.service.bo.GatewayAccessLogsQuery;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;

import java.util.List;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Mapper
public interface GatewayAccessLogsMapperExt extends GatewayAccessLogsMapper {

    List<GatewayAccessLogs> selectByCondition(GatewayAccessLogsQuery query);

    int insertBatch(List<GatewayAccessLogs> logs);
}
