package org.open.gateway.portal.modules.gateway.controller.vo;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(notes = "应用代码", example = "T00951")
    private String appCode;

    @ApiModelProperty(notes = "应用名称", example = "商贸")
    private String appName;

}
