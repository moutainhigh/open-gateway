package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.open.gateway.portal.persistence.po.BaseAccount;

@Mapper
public interface BaseAccountMapperExt extends BaseAccountMapper {

    BaseAccount selectByAccount(String account);

}