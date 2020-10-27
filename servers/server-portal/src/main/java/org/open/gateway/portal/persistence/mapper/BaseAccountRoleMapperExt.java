package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.BaseAccountRole;

import java.util.Collection;

@Mapper
public interface BaseAccountRoleMapperExt extends BaseAccountRoleMapper {

    int deleteByAccountId(@Param("accountId") Integer accountId);

    int insertBatch(Collection<BaseAccountRole> roles);

}