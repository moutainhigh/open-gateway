package org.open.gateway.portal.modules.gateway.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.open.gateway.portal.constants.BizConstants;
import org.open.gateway.portal.validate.annotation.In;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
    private String appCode;

    /**
     * 应用名称
     */
    @NotBlank
    private String appName;

    /**
     * 描述
     */
    private String note;

    /**
     * 注册来源
     */
    @In({BizConstants.REGISTER_FROM.FRONT, BizConstants.REGISTER_FROM.PORTAL})
    private String registerFrom;

    /**
     * token持续时间
     */
    @NotNull
    private Integer accessTokenValidity;

    /**
     * 回调地址
     */
    private String webServerRedirectUri;

    /**
     * 认证授权方式
     */
    private Set<String> authorizedGrantTypes;

}
