package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayAppApi;

public interface GatewayAppApiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GatewayAppApi record);

    int insertSelective(GatewayAppApi record);

    GatewayAppApi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayAppApi record);

    int updateByPrimaryKey(GatewayAppApi record);
}