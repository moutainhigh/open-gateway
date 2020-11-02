package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayAppApi;
import org.open.gateway.portal.persistence.po.GatewayAppApiKey;

public interface GatewayAppApiMapper {
    int deleteByPrimaryKey(GatewayAppApiKey key);

    int insert(GatewayAppApi record);

    int insertSelective(GatewayAppApi record);

    GatewayAppApi selectByPrimaryKey(GatewayAppApiKey key);

    int updateByPrimaryKeySelective(GatewayAppApi record);

    int updateByPrimaryKey(GatewayAppApi record);
}