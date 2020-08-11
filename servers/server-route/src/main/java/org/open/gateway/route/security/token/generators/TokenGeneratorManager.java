package org.open.gateway.route.security.token.generators;

import org.open.gateway.route.entity.oauth2.OAuth2TokenRequest;
import org.open.gateway.route.entity.token.AccessToken;
import org.open.gateway.route.exception.NoSupportedGrantTypeException;
import org.open.gateway.route.service.bo.ClientDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miko on 2020/7/8.
 * token生成管理器
 *
 * @author MIKO
 */
public class TokenGeneratorManager {

    /**
     * token生成器
     */
    private final List<TokenGenerator> tokenGenerators;

    public TokenGeneratorManager(ApplicationContext applicationContext) {
        List<TokenGenerator> tokenGenerators = new ArrayList<>(applicationContext.getBeansOfType(TokenGenerator.class).values());
        AnnotationAwareOrderComparator.sort(tokenGenerators);
        this.tokenGenerators = tokenGenerators;
    }

    /**
     * 生成token
     *
     * @param tokenRequest  token请求
     * @param clientDetails 客户端信息
     * @return access_token
     */
    public AccessToken generate(OAuth2TokenRequest tokenRequest, ClientDetails clientDetails) {
        // 获取授权类型
        String grantType = tokenRequest.getGrant_type();
        // 获取生成器
        TokenGenerator generator = getGenerator(grantType);
        // 生成token
        return generator.generate(tokenRequest, clientDetails);
    }

    /**
     * 获取token生成器
     *
     * @param grantType 授权类型
     * @return 生成器
     */
    public TokenGenerator getGenerator(String grantType) {
        for (TokenGenerator generator : this.tokenGenerators) {
            if (generator.isSupported(grantType)) {
                return generator;
            }
        }
        throw new NoSupportedGrantTypeException(grantType);
    }

}
