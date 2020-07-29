package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;

import java.util.Date;
import java.util.List;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Mapper
public interface GatewayAccessLogsMapperExt extends GatewayAccessLogsMapper {

    List<GatewayAccessLogs> selectList(@Param("ip") String ip, @Param("apiCode") String apiCode, @Param("routeCode") String routeCode,
                                       @Param("requestTimeBegin") Date requestTimeBegin, @Param("requestTimeEnd") Date requestTimeEnd,
                                       @Param("usedTimeBegin") Long usedTimeBegin, @Param("usedTimeEnd") Long usedTimeEnd);

}
