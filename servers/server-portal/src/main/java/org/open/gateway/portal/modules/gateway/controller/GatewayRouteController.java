package org.open.gateway.portal.modules.gateway.controller;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.modules.gateway.service.GatewayRouteService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by miko on 2020/7/15.
 *
 * @author MIKO
 */
@RestController
@AllArgsConstructor
public class GatewayRouteController {

    private final GatewayRouteService gatewayRouteService;


}
