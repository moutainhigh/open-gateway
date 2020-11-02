package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseRole;

public interface BaseRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseRole record);

    int insertSelective(BaseRole record);

    BaseRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseRole record);

    int updateByPrimaryKey(BaseRole record);
}