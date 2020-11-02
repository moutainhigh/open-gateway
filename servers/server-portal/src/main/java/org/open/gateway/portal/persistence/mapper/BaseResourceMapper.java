package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseResource;

public interface BaseResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseResource record);

    int insertSelective(BaseResource record);

    BaseResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseResource record);

    int updateByPrimaryKey(BaseResource record);
}