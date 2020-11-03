package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.vo.PageRequest;

import java.util.Date;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountPagesRequest extends PageRequest {

    @ApiModelProperty(notes = "账户")
    private String account;

    @ApiModelProperty(notes = "手机")
    private String phone;

    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "创建时间开始")
    private Date createDateBegin;

    @ApiModelProperty(notes = "创建时间结束")
    private Date createDateEnd;

    @ApiModelProperty(notes = "状态")
    private Byte status;

}
