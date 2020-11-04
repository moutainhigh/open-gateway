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

    @ApiModelProperty(notes = "账户", example = "admin")
    private String account;

    @ApiModelProperty(notes = "手机", example = "13323511717", required = true)
    private String phone;

    @ApiModelProperty(notes = "邮箱", example = "12345678@qq.com")
    private String email;

    @ApiModelProperty(notes = "创建时间开始", example = "2020-11-03 09:11:30")
    private Date createDateBegin;

    @ApiModelProperty(notes = "创建时间结束", example = "2020-11-04 09:11:30")
    private Date createDateEnd;

    @ApiModelProperty(notes = "状态", example = "1", allowableValues = "0, 1")
    private Integer status;

}
