package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by miko on 10/26/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class RoleSaveRequest {

    @NotBlank
    @ApiModelProperty(notes = "角色代码", example = "R001", required = true)
    private String roleCode;

    @NotBlank
    @ApiModelProperty(notes = "角色名称", example = "系统管理员", required = true)
    private String roleName;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    /**
     * 资源id集合
     */
    @ApiModelProperty(notes = "资源id集合", example = "[1,2,3,4,5]")
    private List<Integer> resourceIds;

}
