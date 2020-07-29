package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayRateLimit;

public interface GatewayRateLimitMapper {
    int insert(GatewayRateLimit record);

    int insertSelective(GatewayRateLimit record);

    GatewayRateLimit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayRateLimit record);

    int updateByPrimaryKey(GatewayRateLimit record);
}