package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayAppQuery;
import org.open.gateway.portal.persistence.po.GatewayApp;

import java.util.List;

@Mapper
public interface GatewayAppMapperExt extends GatewayAppMapper {

    GatewayApp selectByAppCode(@Param("appCode") String appCode);

    GatewayApp selectByClientId(@Param("clientId") String clientId);

    List<GatewayApp> selectByCondition(GatewayAppQuery query);

}