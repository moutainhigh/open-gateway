package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by miko on 9/24/20.
 *
 * @author MIKO
 */
@Getter
@Setter
@ToString
public class AccountLoginRequest {

    /**
     * 账户
     */
    @NotBlank
    private String account;

    /**
     * 加密后的密码
     */
    @NotBlank
    private String password;

    /**
     * 请求的时间
     */
    @NotNull
    private LocalDateTime requestTime;

}
