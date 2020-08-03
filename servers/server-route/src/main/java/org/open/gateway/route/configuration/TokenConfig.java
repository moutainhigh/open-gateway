package org.open.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.security.token.converters.jwt.JwtTokenAuthenticationConverter;
import org.open.gateway.route.security.token.converters.redis.RedisTokenAuthenticationConverter;
import org.open.gateway.route.security.token.generators.TokenGeneratorManager;
import org.open.gateway.route.security.token.generators.jwt.JwtClientCredentialsTokenGenerator;
import org.open.gateway.route.security.token.generators.redis.RedisClientCredentialsTokenGenerator;
import org.open.gateway.route.utils.jwt.JwtProvider;
import org.open.gateway.route.utils.jwt.Jwts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

import java.security.KeyPair;

/**
 * Created by miko on 2020/6/9.
 * jwt配置
 *
 * @author miko
 **/
@Slf4j
@Configuration
public class TokenConfig {

    @Bean
    public TokenGeneratorManager jwtTokenGeneratorManager(ApplicationContext applicationContext) {
        return new TokenGeneratorManager(applicationContext);
    }

    @Configuration
    @AllArgsConstructor
    @ConditionalOnProperty(prefix = "token", value = "store.type", havingValue = "jwt")
    @EnableConfigurationProperties(TokenJwtProperties.class)
    public static class Jwt {

        private final TokenJwtProperties jwtProperties;

        @Bean
        public KeyPair keyPair() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(jwtProperties.getJksFilePath()), jwtProperties.getPassword().toCharArray());
            return keyStoreKeyFactory.getKeyPair(jwtProperties.getAlias(), jwtProperties.getPassword().toCharArray());
        }

        @Bean
        public Jwts jwts() {
            KeyPair keyPair = keyPair();
            return JwtProvider.builder()
                    .keyPair(keyPair)
                    .issuer(this.jwtProperties.getIssuer())
                    .jwts();
        }

        /**
         * jwt token生成器
         */
        @Bean
        public JwtClientCredentialsTokenGenerator clientCredentialsTokenGenerator() {
            return new JwtClientCredentialsTokenGenerator(jwts());
        }

        /**
         * token转换器
         *
         * @return token转换器
         */
        @Bean
        public ServerAuthenticationConverter bearerTokenConverter() {
            return new JwtTokenAuthenticationConverter(jwts());
        }
    }

    @Configuration
    @AllArgsConstructor
    @ConditionalOnProperty(prefix = "token", value = "store.type", havingValue = "redis", matchIfMissing = true)
    public static class Redis {

        /**
         * redis token生成器
         */
        @Bean
        public RedisClientCredentialsTokenGenerator clientCredentialsTokenGenerator(StringRedisTemplate redisTemplate) {
            return new RedisClientCredentialsTokenGenerator(redisTemplate);
        }

        /**
         * token转换器
         *
         * @return token转换器
         */
        @Bean
        public ServerAuthenticationConverter bearerTokenConverter(ReactiveStringRedisTemplate redisTemplate) {
            return new RedisTokenAuthenticationConverter(redisTemplate);
        }

    }

}
