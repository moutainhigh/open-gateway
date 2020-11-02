package org.open.gateway.portal.modules.gateway.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppDisableRequest {

    /**
     * 应用编码
     */
    @NotBlank
    private String appCode;

}
