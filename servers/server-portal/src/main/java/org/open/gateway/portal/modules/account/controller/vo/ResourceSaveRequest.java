package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.validate.annotation.In;

import javax.validation.constraints.NotBlank;

/**
 * Created by miko on 10/28/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class ResourceSaveRequest {

    @NotBlank
    @ApiModelProperty(notes = "资源代码", example = "1001", required = true)
    private String resourceCode;

    @NotBlank
    @ApiModelProperty(notes = "资源名称", example = "用户管理", required = true)
    private String resourceName;

    @In({
            BizConstants.RESOURCE_TYPE.DIRECTORY,
            BizConstants.RESOURCE_TYPE.MENU,
            BizConstants.RESOURCE_TYPE.BUTTON
    })
    @ApiModelProperty(notes = "资源类型", example = "D", required = true, allowableValues = "D, M, B")
    private String resourceType;

    @ApiModelProperty(notes = "父资源代码", example = "10")
    private String parentCode;

    @ApiModelProperty(notes = "权限", example = "account:update:post")
    private String perms;

    @ApiModelProperty(notes = "地址", example = "/aaa/bbb/xxx.html")
    private String url;

    @ApiModelProperty(notes = "排序", example = "1")
    private Integer sort;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

}
