package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.modules.gateway.service.bo.GatewayRouteQuery;
import org.open.gateway.portal.persistence.po.GatewayRoute;

import java.util.List;

@Mapper
public interface GatewayRouteMapperExt extends GatewayRouteMapper {

    List<GatewayRoute> selectByCondition(GatewayRouteQuery query);

    GatewayRoute selectByRouteCode(@Param("routeCode") String routeCode);

}