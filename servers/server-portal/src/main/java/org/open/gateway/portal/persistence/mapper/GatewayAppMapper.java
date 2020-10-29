package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayApp;

public interface GatewayAppMapper {
    int insert(GatewayApp record);

    int insertSelective(GatewayApp record);

    GatewayApp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayApp record);

    int updateByPrimaryKey(GatewayApp record);
}