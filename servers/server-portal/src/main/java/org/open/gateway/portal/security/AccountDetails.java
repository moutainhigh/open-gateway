package org.open.gateway.portal.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * Created by miko on 10/23/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountDetails {

    private Integer id;

    private String account;

    private String password;

    private Set<String> authorities;

    public boolean hasPermission(String authority) {
        return this.authorities.contains(authority);
    }

    public boolean hasAnyPermission(String... authorities) {
        for (String authority : authorities) {
            if (hasPermission(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllPermission(String... authorities) {
        for (String authority : authorities) {
            if (!hasPermission(authority)) {
                return false;
            }
        }
        return true;
    }

}
