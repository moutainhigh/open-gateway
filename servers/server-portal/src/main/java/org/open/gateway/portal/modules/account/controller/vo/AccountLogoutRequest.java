package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountLogoutRequest {

    /**
     * 请求token
     */
    @NotEmpty
    private String token;

}
