package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.GatewayAppApi;

import java.util.Collection;

@Mapper
public interface GatewayAppApiMapperExt extends GatewayAppApiMapper {

    int deleteByAppId(@Param("appId") int appId);

    int insertBatch(Collection<GatewayAppApi> appApis);

}