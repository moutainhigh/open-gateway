package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountLoginResponse {

    /**
     * token
     */
    @ApiModelProperty(notes = "令牌", example = "7eeeefd1-7599-4e92-93dc-febbbb506e0d")
    private String token;

}
