package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayIpLimitApi;
import org.open.gateway.portal.persistence.po.GatewayIpLimitApiKey;

public interface GatewayIpLimitApiMapper {
    int insert(GatewayIpLimitApi record);

    int insertSelective(GatewayIpLimitApi record);

    GatewayIpLimitApi selectByPrimaryKey(GatewayIpLimitApiKey key);

    int updateByPrimaryKeySelective(GatewayIpLimitApi record);

    int updateByPrimaryKey(GatewayIpLimitApi record);
}