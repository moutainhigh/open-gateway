package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseAccountRole;
import org.open.gateway.portal.persistence.po.BaseAccountRoleKey;

public interface BaseAccountRoleMapper {
    int insert(BaseAccountRole record);

    int insertSelective(BaseAccountRole record);

    BaseAccountRole selectByPrimaryKey(BaseAccountRoleKey key);

    int updateByPrimaryKeySelective(BaseAccountRole record);

    int updateByPrimaryKey(BaseAccountRole record);
}