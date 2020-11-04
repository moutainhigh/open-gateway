package org.open.gateway.portal.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by miko on 11/4/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("spring.security")
public class PortalSecurityProperties {

    /**
     * 自定义访问权限配置
     */
    private Map<AccessEnum, String[]> access;

    public String[] getPermitAll() {
        return access.get(AccessEnum.permitAll);
    }

    @Getter
    @AllArgsConstructor
    public enum AccessEnum {

        permitAll("permitAll"),
        authenticated("authenticated"),
        denyAll("denyAll");

        private final String value;


    }

}
