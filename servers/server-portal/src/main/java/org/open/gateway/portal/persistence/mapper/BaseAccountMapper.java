package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseAccount;

public interface BaseAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseAccount record);

    int insertSelective(BaseAccount record);

    BaseAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseAccount record);

    int updateByPrimaryKey(BaseAccount record);
}