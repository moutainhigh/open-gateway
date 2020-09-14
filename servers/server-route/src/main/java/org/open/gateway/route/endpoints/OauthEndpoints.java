package org.open.gateway.route.endpoints;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.gateway.common.base.constants.EndpointsConstants;
import open.gateway.common.base.constants.OAuth2Constants;
import open.gateway.common.utils.Dates;
import org.open.gateway.route.entity.oauth2.OAuth2AuthorizeRequest;
import org.open.gateway.route.entity.oauth2.OAuth2TokenRequest;
import org.open.gateway.route.entity.oauth2.OAuth2TokenResponse;
import org.open.gateway.route.exception.InvalidClientSecretException;
import org.open.gateway.route.service.ClientDetailsService;
import org.open.gateway.route.service.TokenService;
import org.open.gateway.route.service.bo.ClientDetails;
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
    private final TokenService tokenService;

    /**
     * 认证请求
     */
    @PostMapping(EndpointsConstants.AUTHORIZE)
    @ResponseBody
    public Mono<String> authorize(OAuth2AuthorizeRequest authorizeRequest) {
        return Mono.empty();
    }

    /**
     * 获取token
     */
    @PostMapping(EndpointsConstants.TOKEN)
    @ResponseBody
    public Mono<OAuth2TokenResponse> token(OAuth2TokenRequest tokenRequest) {
        // 校验token请求
        checkTokenRequest(tokenRequest);
        return clientDetailsService.loadClientByClientId(tokenRequest.getClient_id()) // 根据client_id获取client信息
                .filter(cd -> this.checkClientSecret(tokenRequest, cd)) // 校验secret
                .flatMap(cd ->
                        tokenService.loadClientTokenByClientId(cd.getClientId()) // 从数据库查询该客户端已经存在的token
                                .filter(token -> !token.isExpired()) // 过滤没有过期的
                                .map(token -> buildTokenResponse(token.getToken(), Dates.toTimestamp(token.getExpireTime()))) // 构建返回对象
                                .doOnNext(response -> log.info("Client id:{} exists token:{} expire_at:{}", tokenRequest.getClient_id(), response.getAccess_token(), response.getExpire_at()))
                                .switchIfEmpty(
                                        Mono.defer(() -> this.tokenService.generate(cd)) // 重新生成token
                                                .flatMap(accessToken -> tokenService.saveClientToken(cd.getClientId(), accessToken.getToken(), accessToken.getExpireAt()).thenReturn(accessToken)) // 保存token
                                                .map(accessToken -> buildTokenResponse(accessToken.getToken(), accessToken.getExpireAt()))
                                                .doOnSuccess(response -> log.info("Generated token:{} expire_in:{} with client_id:{}", response.getAccess_token(), response.getExpire_at(), tokenRequest.getClient_id()))
                                ) // 没有或者过期时候生成一个token
                );
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
     * @param token    token
     * @param expireIn 过期时间
     * @return 返回数据
     */
    private OAuth2TokenResponse buildTokenResponse(String token, Long expireAt) {
        OAuth2TokenResponse response = new OAuth2TokenResponse();
        response.setAccess_token(token);
        response.setToken_type(OAuth2Constants.TOKEN_PREFIX);
        response.setExpire_at(expireAt);
        return response;
    }

}
