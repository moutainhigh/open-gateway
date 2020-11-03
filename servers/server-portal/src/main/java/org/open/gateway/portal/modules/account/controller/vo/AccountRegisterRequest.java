package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
@ApiModel(description = "用户注册请求参数")
public class AccountRegisterRequest {

    /**
     * 账户
     */
    @NotBlank
    @ApiModelProperty(notes = "账户", example = "Jack Ma", required = true)
    private String account;

    /**
     * 密码
     */
    @NotBlank
    @ApiModelProperty(notes = "密码", example = "123456", required = true)
    private String password;

    /**
     * 手机号码
     */
    @Pattern(regexp = "[0-9]{11}")
    @NotBlank
    @ApiModelProperty(notes = "手机", example = "13323511717", required = true)
    private String phone;

    /**
     * 邮箱
     */
    @Email
    @ApiModelProperty(notes = "邮箱", example = "12345678@qq.com")
    private String email;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

}
