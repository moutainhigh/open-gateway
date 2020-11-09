package org.open.gateway.portal.modules.gateway.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.validate.annotation.In;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static org.open.gateway.portal.constants.BizConstants.REGISTER_FROM.FRONT;
import static org.open.gateway.portal.constants.BizConstants.REGISTER_FROM.PORTAL;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppSaveRequest {

    /**
     * 应用编码
     */
    @NotBlank
    @ApiModelProperty(notes = "应用代码", example = "T00951", required = true)
    private String appCode;

    /**
     * 应用名称
     */
    @NotBlank
    @ApiModelProperty(notes = "应用名称", example = "商贸", required = true)
    private String appName;

    /**
     * 描述
     */
    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    /**
     * 注册来源
     */
    @In({FRONT, PORTAL})
    @ApiModelProperty(notes = "注册来源", example = "front", required = true, allowableValues = "front,portal")
    private String registerFrom;

    /**
     * token持续时间
     */
    @NotNull
    @ApiModelProperty(notes = "token有效时间 单位:毫秒", example = "43200")
    private Integer accessTokenValidity;

    /**
     * 回调地址
     */
    @ApiModelProperty(notes = "回调地址", example = "https://aaa/bbb/ccc/xxx.html")
    private String webServerRedirectUri;

    /**
     * 认证授权方式
     */
    @ApiModelProperty(notes = "认证方式", example = "[client_credentials]", allowableValues = "client_credentials")
    private Set<String> authorizedGrantTypes;

    /**
     * 接口分组id集合
     */
    @ApiModelProperty(notes = "接口分组id集合", example = "[1,2]")
    private Set<Integer> groupIds;

    /**
     * 接口id集合
     */
    @ApiModelProperty(notes = "接口id集合", example = "[1,2,3,4]")
    private Set<Integer> apiIds;

}
