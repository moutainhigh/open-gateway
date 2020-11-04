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
public class AccountRegisterResponse {

    @ApiModelProperty(notes = "id", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "账户", example = "admin")
    private String account;

    @ApiModelProperty(notes = "注册ip", example = "114.86.161.87")
    private String registerIp;

    @ApiModelProperty(notes = "状态", example = "1", allowableValues = "0, 1")
    private Integer status;

    @ApiModelProperty(notes = "手机", example = "13323511717")
    private String phone;

    @ApiModelProperty(notes = "邮箱", example = "12345678@qq.com")
    private String email;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

}
