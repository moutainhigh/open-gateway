package org.open.gateway.route.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.security.token.converters.jwt.JwtTokenAuthenticationConverter;
import org.open.gateway.route.security.token.converters.redis.RedisTokenAuthenticationConverter;
import org.open.gateway.route.security.token.generators.TokenGeneratorManager;
import org.open.gateway.route.security.token.generators.jwt.JwtClientCredentialsTokenGenerator;
import org.open.gateway.route.security.token.generators.redis.RedisClientCredentialsTokenGenerator;
import org.open.gateway.route.utils.jwt.JwtProvider;
import org.open.gateway.route.utils.jwt.Jwts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
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
    @ConditionalOnProperty(value = "token.store.type", havingValue = "jwt")
    @EnableConfigurationProperties(Jwt.TokenJwtProperties.class)
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

        /**
         * Created by miko on 2020/7/10.
         *
         * @author MIKO
         */
        @Getter
        @Setter
        @ToString
        @ConfigurationProperties(prefix = "token.jwt")
        public static class TokenJwtProperties {

            /**
             * 密钥文件路径
             */
            private String jksFilePath;

            /**
             * 生成rsa密钥的密码
             */
            private String password;

            /**
             * 签发者
             */
            private String issuer;

            /**
             * 别名
             */
            private String alias;

        }
    }

    @Configuration
    @AllArgsConstructor
    @ConditionalOnProperty(value = "token.store.type", havingValue = "redis", matchIfMissing = true)
    public static class Redis {

        /**
         * redis token生成器
         */
        @Bean
        public RedisClientCredentialsTokenGenerator clientCredentialsTokenGenerator(ReactiveStringRedisTemplate redisTemplate) {
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
