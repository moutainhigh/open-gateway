package org.open.gateway.portal.modules.gateway.controller;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.Endpoints;
import org.open.gateway.portal.modules.gateway.service.GatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
@RestController
@AllArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    @PostMapping(Endpoints.GATEWAY_ROUTES_REFRESH)
    public String refreshRoutes(@RequestParam(name = "apiCodes", required = false) Set<String> apiCodes) {
        this.gatewayService.refreshRoutes(apiCodes);
        return "ok";
    }

    @PostMapping(Endpoints.GATEWAY_RESOURCES_REFRESH)
    public String refreshResources(@RequestParam(name = "clientIds", required = false) Set<String> clientIds) {
        this.gatewayService.refreshResources(clientIds);
        return "ok";
    }

    @PostMapping(Endpoints.GATEWAY_IP_LIMITS_REFRESH)
    public String refreshIpLimits(@RequestParam(name = "ipLimits", required = false) Set<String> ipLimits) {
        this.gatewayService.refreshIpLimits(ipLimits);
        return "ok";
    }

}
