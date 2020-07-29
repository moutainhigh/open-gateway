package org.open.gateway.route.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by miko on 2020/7/10.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "token.jwt")
public class TokenJwtProperties {

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
