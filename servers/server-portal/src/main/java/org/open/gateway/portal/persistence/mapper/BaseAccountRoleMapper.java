package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseAccountRole;

public interface BaseAccountRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseAccountRole record);

    int insertSelective(BaseAccountRole record);

    BaseAccountRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseAccountRole record);

    int updateByPrimaryKey(BaseAccountRole record);
}