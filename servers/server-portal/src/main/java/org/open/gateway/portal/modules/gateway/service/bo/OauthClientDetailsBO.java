package org.open.gateway.portal.modules.gateway.service.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OauthClientDetailsBO {

    private String clientId;

    private String clientSecret;

    private String resourceIds;

    private List<String> scope;

    private List<String> authorizedGrantTypes;

    private String webServerRedirectUri;

    private List<String> authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String additionalInformation;

    private String autoapprove;

}