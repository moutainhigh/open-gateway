package org.open.gateway.portal.persistence.mapper;

import org.open.gateway.portal.persistence.po.BaseOperationLogs;

public interface BaseOperationLogsMapper {
    int insert(BaseOperationLogs record);

    int insertSelective(BaseOperationLogs record);

    BaseOperationLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseOperationLogs record);

    int updateByPrimaryKey(BaseOperationLogs record);
}