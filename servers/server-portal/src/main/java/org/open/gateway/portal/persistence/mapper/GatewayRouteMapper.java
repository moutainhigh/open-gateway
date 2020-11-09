package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayRoute;

public interface GatewayRouteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GatewayRoute record);

    int insertSelective(GatewayRoute record);

    GatewayRoute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayRoute record);

    int updateByPrimaryKey(GatewayRoute record);
}