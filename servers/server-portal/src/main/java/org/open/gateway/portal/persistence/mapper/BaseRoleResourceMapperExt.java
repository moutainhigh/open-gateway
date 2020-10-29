package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.BaseRoleResource;

import java.util.Collection;

@Mapper
public interface BaseRoleResourceMapperExt extends BaseAccountRoleMapper {

    int deleteByRoleId(@Param("roleId") Integer roleId);

    int insertBatch(Collection<BaseRoleResource> resources);

}