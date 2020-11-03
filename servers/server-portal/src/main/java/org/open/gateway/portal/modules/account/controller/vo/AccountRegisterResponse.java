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

    @ApiModelProperty(notes = "主键")
    private Integer id;

    @ApiModelProperty(notes = "账户")
    private String account;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "注册ip")
    private String registerIp;

    @ApiModelProperty(notes = "状态")
    private Byte status;

    @ApiModelProperty(notes = "手机")
    private String phone;

    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "备注")
    private String note;

}
