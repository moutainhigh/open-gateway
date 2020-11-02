package org.open.gateway.portal.modules.gateway.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.validate.annotation.In;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppEnableRequest {

    /**
     * 应用编码
     */
    @NotBlank
    private String appCode;

}
