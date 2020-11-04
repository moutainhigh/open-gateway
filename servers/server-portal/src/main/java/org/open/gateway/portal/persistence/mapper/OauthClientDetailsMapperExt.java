package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.open.gateway.portal.persistence.po.OauthClientDetails;

@Mapper
public interface OauthClientDetailsMapperExt extends OauthClientDetailsMapper {

    OauthClientDetails selectByClientId(@Param("clientId") String clientId);

}