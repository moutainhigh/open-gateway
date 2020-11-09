package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayGroup;

public interface GatewayGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GatewayGroup record);

    int insertSelective(GatewayGroup record);

    GatewayGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayGroup record);

    int updateByPrimaryKey(GatewayGroup record);
}