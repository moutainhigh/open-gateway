package org.open.gateway.portal.modules.gateway.controller.vo;

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

    private Integer id;

    private String appName;

    private String clientId;

    private String clientSecret;

    private String registerFrom;

    private Byte status;

    private String note;

    private Date createTime;

    private String createPerson;

    private Date updateTime;

    private String updatePerson;

    /**
     * 回调地址
     */
    private String webServerRedirectUri;

    /**
     * 认证方式
     */
    private List<String> authorizedGrantTypes;

    /**
     * 范围
     */
    private List<String> scopes;

}
