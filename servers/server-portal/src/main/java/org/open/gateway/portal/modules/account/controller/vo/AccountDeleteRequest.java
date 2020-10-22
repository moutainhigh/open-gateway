package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
    private String account;

}
