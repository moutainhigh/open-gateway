package org.open.gateway.portal.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.open.gateway.portal.persistence.po.BaseResource;

import java.util.List;

@Mapper
public interface BaseResourceMapperExt extends BaseResourceMapper {

    List<BaseResource> selectByAccount(String account);

}