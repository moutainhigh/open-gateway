package org.open.gateway.portal.modules.account.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
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
public class AccountRegisterRequest {

    /**
     * 账户
     */
    @NotEmpty
    private String account;

    /**
     * 加密后的密码
     */
    @NotEmpty
    private String password;

    /**
     * 请求的时间
     */
    @NotNull
    private LocalDateTime requestTime;

}
