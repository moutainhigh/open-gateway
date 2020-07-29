package org.open.gateway.portal.controller;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.service.GatewayService;
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

    @PostMapping(EndPoints.GATEWAY_ROUTES_REFRESH)
    public String refreshRoutes(@RequestParam(name = "apiCodes", required = false) Set<String> apiCodes) {
        this.gatewayService.refreshRoutes(apiCodes);
        return "ok";
    }

    @PostMapping(EndPoints.GATEWAY_RESOURCES_REFRESH)
    public String refreshResources(@RequestParam(name = "clientIds", required = false) Set<String> clientIds) {
        this.gatewayService.refreshResources(clientIds);
        return "ok";
    }

}
