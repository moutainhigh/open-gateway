package org.open.gateway.portal.modules.account.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
@ApiModel(description = "用户登录请求参数")
public class AccountLoginRequest {

    /**
     * 账户
     */
    @NotBlank
    @ApiModelProperty(notes = "账户", example = "admin", required = true)
    private String account;

    /**
     * 加密后的密码
     */
    @NotBlank
    @ApiModelProperty(notes = "密码密文, 加密规则:des(password, md5(requestTime))", example = "86a17a4fe3d381cfb821ce7c240f967b", required = true)
    private String password;

    /**
     * 请求的时间
     */
    @NotNull
    @ApiModelProperty(notes = "请求时间", example = "2020-11-02 16:29:58", required = true)
    private LocalDateTime requestTime;

}
