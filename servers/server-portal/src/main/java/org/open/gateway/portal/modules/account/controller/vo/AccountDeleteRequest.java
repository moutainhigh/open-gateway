package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 10/22/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountDeleteRequest {

    /**
     * 账户
     */
    @NotBlank
    @ApiModelProperty(notes = "账户", example = "admin", required = true)
    private String account;

}
