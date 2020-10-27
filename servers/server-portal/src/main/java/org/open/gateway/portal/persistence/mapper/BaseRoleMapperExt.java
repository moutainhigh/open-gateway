package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.BaseRole;

import java.util.List;

@Mapper
public interface BaseRoleMapperExt extends BaseRoleMapper {

    List<BaseRole> selectRolesByAccount(@Param("account") String account);

    BaseRole selectRoleByCode(@Param("roleCode") String roleCode);

}