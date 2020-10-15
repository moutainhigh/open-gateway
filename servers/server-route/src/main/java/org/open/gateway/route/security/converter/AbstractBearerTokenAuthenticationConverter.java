package org.open.gateway.route.security.converter;

import org.open.gateway.route.constants.OAuth2Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by miko on 2020/7/22.
 * 基础Bearer token转换器
 *
 * @author MIKO
 */
public abstract class AbstractBearerTokenAuthenticationConverter implements ServerAuthenticationConverter {

    /**
     * 是否允许从get请求的url参数中获取token
     */
    private boolean allowUriQueryParameter = false;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(getTokenString(exchange.getRequest()))
                .flatMap(this::parseToken);
    }

    /**
     * 解析请求的token信息
     *
     * @param tokenString token字符串
     * @return 交给认证管理器的信息
     */
    protected abstract Mono<Authentication> parseToken(String tokenString);

    /**
     * 获取token信息
     *
     * @param request 请求头
     * @return token字符串
     */
    private String getTokenString(ServerHttpRequest request) {
        // 请求头中的token
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request.getHeaders());
        if (authorizationHeaderToken != null) {
            return authorizationHeaderToken;
        }
        // get请求url参数的token
        String parameterToken = request.getQueryParams().getFirst(OAuth2Constants.TOKEN_PARAM_NAME);
        if (parameterToken != null && isParameterTokenSupportedForRequest(request)) {
            return parameterToken;
        }
        return null;
    }

    /**
     * 获取请求头中的token信息
     *
     * @param headers 请求头
     * @return token字符串
     */
    private String resolveFromAuthorizationHeader(HttpHeaders headers) {
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith(OAuth2Constants.TOKEN_PREFIX)) {
            return authorization.substring(OAuth2Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 是否支持从get请求中的url中获取token
     *
     * @param request 请求参数
     * @return true支持，false不支持
     */
    protected boolean isParameterTokenSupportedForRequest(ServerHttpRequest request) {
        return this.allowUriQueryParameter && HttpMethod.GET.equals(request.getMethod());
    }

    public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
        this.allowUriQueryParameter = allowUriQueryParameter;
    }

}
