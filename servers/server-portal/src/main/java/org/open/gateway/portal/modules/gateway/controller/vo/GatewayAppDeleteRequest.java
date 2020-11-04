package org.open.gateway.portal.modules.gateway.controller.vo;

import io.swagger.annotations.ApiModelProperty;
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
public class GatewayAppDeleteRequest {

    /**
     * 应用编码
     */
    @NotBlank
    @ApiModelProperty(notes = "应用代码", example = "T00951", required = true)
    private String appCode;

}
