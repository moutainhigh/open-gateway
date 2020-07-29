package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseRoleResource;
import org.open.gateway.portal.persistence.po.BaseRoleResourceKey;

public interface BaseRoleResourceMapper {
    int insert(BaseRoleResource record);

    int insertSelective(BaseRoleResource record);

    BaseRoleResource selectByPrimaryKey(BaseRoleResourceKey key);

    int updateByPrimaryKeySelective(BaseRoleResource record);

    int updateByPrimaryKey(BaseRoleResource record);
}