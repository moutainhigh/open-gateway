package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountRegisterRequest {

    /**
     * 账户
     */
    @NotEmpty
    private String account;

    /**
     * 加密后的密码
     */
    @NotEmpty
    private String password;

    /**
     * 手机号码
     */
    @Pattern(regexp = "[0-9]{13}")
    @NotEmpty
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String note;

}
