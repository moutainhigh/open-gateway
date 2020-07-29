package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayIpLimit;

public interface GatewayIpLimitMapper {
    int insert(GatewayIpLimit record);

    int insertSelective(GatewayIpLimit record);

    GatewayIpLimit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayIpLimit record);

    int updateByPrimaryKey(GatewayIpLimit record);
}