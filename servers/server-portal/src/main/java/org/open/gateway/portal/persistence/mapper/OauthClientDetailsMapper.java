package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.OauthClientDetails;

public interface OauthClientDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OauthClientDetails record);

    int insertSelective(OauthClientDetails record);

    OauthClientDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OauthClientDetails record);

    int updateByPrimaryKey(OauthClientDetails record);
}