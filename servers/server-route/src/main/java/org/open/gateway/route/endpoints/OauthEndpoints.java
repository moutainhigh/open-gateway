package org.open.gateway.route.endpoints;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.EndpointsConstants;
import open.gateway.common.base.constants.OAuth2Constants;
import open.gateway.common.base.entity.oauth2.OAuth2AuthorizeRequest;
import open.gateway.common.base.entity.oauth2.OAuth2TokenRequest;
import open.gateway.common.base.entity.oauth2.OAuth2TokenResponse;
import open.gateway.common.base.entity.token.AccessToken;
import org.open.gateway.route.exception.InvalidClientSecretException;
import org.open.gateway.route.security.client.ClientDetails;
import org.open.gateway.route.security.client.ClientDetailsService;
import org.open.gateway.route.security.token.generators.TokenGeneratorManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/7.
 *
 * @author MIKO
 */
@Slf4j
@Controller
@AllArgsConstructor
public class OauthEndpoints {

    private final ClientDetailsService clientDetailsService;

    private final TokenGeneratorManager tokenGeneratorManager;

    /**
     * 认证请求
     */
    @PostMapping(EndpointsConstants.AUTHORIZE)
    @ResponseBody
    public Mono<String> authorize(OAuth2AuthorizeRequest authorizeRequest) {
        // TODO
        return Mono.just("No support authorize");
    }

    /**
     * 获取token
     */
    @PostMapping(EndpointsConstants.TOKEN)
    @ResponseBody
    public Mono<OAuth2TokenResponse> token(OAuth2TokenRequest tokenRequest) {
        // 校验token请求
        checkTokenRequest(tokenRequest);
        // 根据client_id获取client信息
        Mono<ClientDetails> clientDetails = clientDetailsService.loadClientByClientId(tokenRequest.getClient_id());
        return clientDetails
                .filter(cd -> this.checkClientSecret(tokenRequest, cd)) // 校验secret
                .map(cd -> this.tokenGeneratorManager.generate(tokenRequest, cd)) // 生成token
                .doOnSuccess(token -> log.info("Generated token:{} expire_in:{} with client_id:{}", token.getToken(), token.getExpire_in(), tokenRequest.getClient_id()))
                .map(this::buildTokenResponse);
    }

    /**
     * 校验ClientSecret
     *
     * @param tokenRequest  用户token请求参数
     * @param clientDetails 客户端信息
     */
    private boolean checkClientSecret(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails) {
        if (!tokenRequest.getClient_secret().equals(clientDetails.getClientSecret())) {
            throw new InvalidClientSecretException(tokenRequest.getClient_secret());
        }
        return true;
    }

    /**
     * 校验token请求
     *
     * @param request 请求信息
     */
    private void checkTokenRequest(OAuth2TokenRequest request) {
        Assert.notNull(request.getGrant_type(), "grant_type is required");
        Assert.notNull(request.getClient_id(), "client_id is required");
        Assert.notNull(request.getClient_secret(), "client_secret is required");
    }

    /**
     * 构建token返回数据
     *
     * @param token token信息
     * @return 返回数据
     */
    private OAuth2TokenResponse buildTokenResponse(AccessToken token) {
        OAuth2TokenResponse response = new OAuth2TokenResponse();
        response.setAccess_token(token.getToken());
        response.setToken_type(OAuth2Constants.TOKEN_PREFIX);
        response.setExpire_in(token.getExpire_in());
        response.setJti(token.getJti());
        return response;
    }

}
