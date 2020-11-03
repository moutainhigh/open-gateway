package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Created by miko on 10/22/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountUpdateRequest {

    /**
     * 账户
     */
    @NotBlank
    @ApiModelProperty(notes = "账户", example = "admin", required = true)
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码", example = "admin123")
    private String password;

    /**
     * 手机号码
     */
    @Pattern(regexp = "[0-9]{11}")
    @ApiModelProperty(notes = "手机", example = "13323335454")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(notes = "邮箱", example = "7741921923@163.com")
    private String email;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    /**
     * 角色id集合
     */
    @ApiModelProperty(notes = "角色id")
    private List<Integer> roleIds;

}
