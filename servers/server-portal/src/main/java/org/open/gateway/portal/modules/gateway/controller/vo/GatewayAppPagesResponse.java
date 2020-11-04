package org.open.gateway.portal.modules.gateway.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class GatewayAppPagesResponse {

    @ApiModelProperty(notes = "id", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "应用代码", example = "T00951")
    private String appCode;

    @ApiModelProperty(notes = "应用名称", example = "商贸")
    private String appName;

    @ApiModelProperty(notes = "客户端id", example = "cf1db598047446f4afe03dd5f59ad1c2")
    private String clientId;

    @ApiModelProperty(notes = "客户端密钥", example = "JzOrqYzPMneOJYsxpnK5jzzk2274VwX64Jm93QgnVENYv1QK")
    private String clientSecret;

    @ApiModelProperty(notes = "注册来源", example = "front", allowableValues = "front,portal")
    private String registerFrom;

    @ApiModelProperty(notes = "状态", example = "1", allowableValues = "0, 1")
    private Integer status;

    @ApiModelProperty(notes = "备注", example = "这个是备注")
    private String note;

    @ApiModelProperty(notes = "创建时间", example = "2020-11-03 09:11:30")
    private Date createTime;

    @ApiModelProperty(notes = "创建人", example = "admin")
    private String createPerson;

    @ApiModelProperty(notes = "修改时间", example = "2020-11-03 09:11:30")
    private Date updateTime;

    @ApiModelProperty(notes = "修改人", example = "admin")
    private String updatePerson;

    /**
     * 回调地址
     */
    @ApiModelProperty(notes = "回调地址", example = "https://aaa/bbb/ccc/xxx.html")
    private String webServerRedirectUri;

    /**
     * 认证方式
     */
    @ApiModelProperty(notes = "认证方式", example = "[client_credentials]")
    private List<String> authorizedGrantTypes;

    /**
     * 范围
     */
    @ApiModelProperty(notes = "范围")
    private List<String> scopes;

}
