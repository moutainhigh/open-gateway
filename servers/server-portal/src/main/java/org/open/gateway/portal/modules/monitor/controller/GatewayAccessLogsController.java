package org.open.gateway.portal.modules.monitor.controller;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.modules.monitor.controller.vo.AccessLogsPagesRequest;
import org.open.gateway.portal.modules.monitor.controller.vo.AccessLogsPagesResponse;
import org.open.gateway.portal.persistence.mapper.GatewayAccessLogsMapperExt;
import org.open.gateway.portal.persistence.po.GatewayAccessLogs;
import org.open.gateway.portal.utils.Beans;
import org.open.gateway.portal.vo.Result;
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
    public Result pages(@Valid @RequestBody AccessLogsPagesRequest request) {
        // 查询分页列表
        Page<GatewayAccessLogs> logs = request.doSelectPage(() -> gatewayAccessLogsMapper.selectList(
                request.getIp(), request.getApiCode(), request.getRouteCode(),
                request.getRequestTimeBegin(), request.getRequestTimeEnd(),
                request.getUsedTimeBegin(), request.getUsedTimeEnd()
        ));
        // 转换对象
        List<AccessLogsPagesResponse> responses = logs.stream()
                .map(log -> Beans.from(log).convert(AccessLogsPagesResponse::new))
                .collect(Collectors.toList());
        return Result.data(responses).pageInfo(logs).ok();
    }

}
