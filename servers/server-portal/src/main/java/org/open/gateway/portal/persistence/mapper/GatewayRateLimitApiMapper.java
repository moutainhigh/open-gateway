package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayRateLimitApi;
import org.open.gateway.portal.persistence.po.GatewayRateLimitApiKey;

public interface GatewayRateLimitApiMapper {
    int insert(GatewayRateLimitApi record);

    int insertSelective(GatewayRateLimitApi record);

    GatewayRateLimitApi selectByPrimaryKey(GatewayRateLimitApiKey key);

    int updateByPrimaryKeySelective(GatewayRateLimitApi record);

    int updateByPrimaryKey(GatewayRateLimitApi record);
}