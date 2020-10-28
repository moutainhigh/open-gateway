package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.BaseResource;

import java.util.List;

@Mapper
public interface BaseResourceMapperExt extends BaseResourceMapper {

    List<BaseResource> selectAll();

    List<BaseResource> selectByAccountAndResourceType(@Param("account") String account, @Param("resourceType") String resourceType);

    List<BaseResource> selectByRole(@Param("roleCode") String roleCode);

    BaseResource selectByCode(@Param("resourceCode") String resourceCode);

}