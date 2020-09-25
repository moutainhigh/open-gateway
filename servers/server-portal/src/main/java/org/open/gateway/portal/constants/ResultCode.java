package org.open.gateway.portal.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miko on 2020/7/23.
 *
 * @author MIKO
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 账户不存在
     */
    ACCOUNT_NOT_EXISTS("1000", "账户不存在"),
    /**
     * 无效的密码
     */
    ACCOUNT_INVALID_PASSWORD("1001", "无效的密码"),
    /**
     * 帐户不可用
     */
    ACCOUNT_NOT_AVAILABLE("1002", "帐户不可用"),
    /**
     * 成功
     */
    SUCCESS("0000", "OK");

    private final String code;
    private final String message;

}
