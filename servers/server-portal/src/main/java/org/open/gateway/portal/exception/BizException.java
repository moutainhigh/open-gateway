package org.open.gateway.portal.exception;

import org.open.gateway.portal.constants.ResultCode;

/**
 * Created by miko on 2019/10/29.
 * 业务异常
 *
 * @author MIKO
 */
public class BizException extends RuntimeException {

    /**
     * 异常代码
     */
    private final String code;

    public BizException(String code) {
        super(code);
        this.code = code;
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
