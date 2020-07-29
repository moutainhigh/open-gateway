package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.GatewayApiClassification;

public interface GatewayApiClassificationMapper {
    int insert(GatewayApiClassification record);

    int insertSelective(GatewayApiClassification record);

    GatewayApiClassification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GatewayApiClassification record);

    int updateByPrimaryKey(GatewayApiClassification record);
}