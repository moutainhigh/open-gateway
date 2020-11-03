package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 10/27/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountPagesResponse {

    @ApiModelProperty(notes = "id")
    private Integer id;

    @ApiModelProperty(notes = "账户")
    private String account;

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

    @ApiModelProperty(notes = "创建时间")
    private Date createTime;

    @ApiModelProperty(notes = "创建人")
    private String createPerson;

    @ApiModelProperty(notes = "修改时间")
    private Date updateTime;

    @ApiModelProperty(notes = "修改人")
    private String updatePerson;

}
