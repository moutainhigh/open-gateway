package org.open.gateway.portal.modules.monitor.controller;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.modules.monitor.controller.vo.AccessLogsPagesRequest;
import org.open.gateway.portal.modules.monitor.controller.vo.AccessLogsPagesResponse;
import org.open.gateway.portal.modules.monitor.service.bo.GatewayAccessLogsQuery;
import org.open.gateway.portal.persistence.mapper.GatewayAccessLogsMapperExt;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;
import org.open.gateway.portal.utils.Beans;
import org.open.gateway.portal.vo.PageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/23.
 * 网关日志接口
 *
 * @author MIKO
 */
@RestController
@AllArgsConstructor
public class GatewayAccessLogsController {

    private final GatewayAccessLogsMapperExt gatewayAccessLogsMapper;

    @PostMapping(Endpoints.MONITOR_LOGS_PAGES)
    public PageResponse<List<AccessLogsPagesResponse>> pages(@Valid @RequestBody AccessLogsPagesRequest request) {
        GatewayAccessLogsQuery query = new GatewayAccessLogsQuery();
        query.setIp(request.getIp());
        query.setApiCode(request.getApiCode());
        query.setRouteCode(request.getRouteCode());
        query.setRequestTimeBegin(request.getRequestTimeBegin());
        query.setRequestTimeEnd(request.getRequestTimeEnd());
        query.setUsedTimeBegin(request.getUsedTimeBegin());
        query.setUsedTimeEnd(request.getUsedTimeEnd());
        // 查询分页列表
        Page<GatewayAccessLogs> logs = request.doSelectPage(() -> gatewayAccessLogsMapper.selectByCondition(query));
        // 转换对象
        List<AccessLogsPagesResponse> responses = logs.stream()
                .map(log -> Beans.from(log).convert(AccessLogsPagesResponse::new))
                .collect(Collectors.toList());
        return PageResponse.data(responses).pageInfo(logs).ok();
    }

}
