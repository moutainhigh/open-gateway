package org.open.gateway.portal.modules.gateway.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.vo.PageRequest;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppPagesRequest extends PageRequest {

    private String clientId;

    private String appName;

}
