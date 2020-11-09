package org.open.gateway.portal.modules.gateway.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 11/9/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayRouteQuery {

    private String routeCode;

    private String routePath;

    private Integer routeType;

}
