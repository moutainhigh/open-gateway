package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.open.gateway.portal.modules.account.service.bo.BaseAccountQuery;
import org.open.gateway.portal.persistence.po.BaseAccount;

import java.util.List;

@Mapper
public interface BaseAccountMapperExt extends BaseAccountMapper {

    BaseAccount selectByAccount(String account);

    List<BaseAccount> selectByCondition(BaseAccountQuery query);

}