package org.open.gateway.portal.modules.route.controller;

import lombok.AllArgsConstructor;
import org.open.gateway.portal.constants.EndPoints;
import org.open.gateway.portal.modules.route.controller.vo.RoutePagesRequest;
import org.open.gateway.portal.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
@RestController
@AllArgsConstructor
public class GatewayRouteController {

    @PostMapping(EndPoints.ROUTES_PAGES)
    public Result pages(@Valid @RequestBody RoutePagesRequest request) {
        // TODO
        return Result.ok();
    }

}
