package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayApi;

public interface GatewayApiMapper {
    int insert(GatewayApi record);

    int insertSelective(GatewayApi record);

    GatewayApi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayApi record);

    int updateByPrimaryKey(GatewayApi record);
}