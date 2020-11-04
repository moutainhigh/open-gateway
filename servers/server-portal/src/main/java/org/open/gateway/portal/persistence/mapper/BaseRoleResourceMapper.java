package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseRoleResource;

public interface BaseRoleResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseRoleResource record);

    int insertSelective(BaseRoleResource record);

    BaseRoleResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseRoleResource record);

    int updateByPrimaryKey(BaseRoleResource record);
}