package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.OauthClientToken;

public interface OauthClientTokenMapper {
    int insert(OauthClientToken record);

    int insertSelective(OauthClientToken record);

    OauthClientToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OauthClientToken record);

    int updateByPrimaryKey(OauthClientToken record);
}