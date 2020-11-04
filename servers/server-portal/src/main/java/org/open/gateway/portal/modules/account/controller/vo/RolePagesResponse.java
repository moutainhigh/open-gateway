package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class RolePagesResponse {

    @ApiModelProperty(notes = "id", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "角色代码", example = "R001")
    private String roleCode;

    @ApiModelProperty(notes = "角色名称", example = "系统管理员")
    private String roleName;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    @ApiModelProperty(notes = "状态", example = "1", allowableValues = "0, 1")
    private Integer status;

    @ApiModelProperty(notes = "创建时间", example = "2020-11-03 09:11:30")
    private Date createTime;

    @ApiModelProperty(notes = "创建人", example = "admin")
    private String createPerson;

    @ApiModelProperty(notes = "修改时间", example = "2020-11-03 09:11:30")
    private Date updateTime;

    @ApiModelProperty(notes = "修改人", example = "admin")
    private String updatePerson;

}
