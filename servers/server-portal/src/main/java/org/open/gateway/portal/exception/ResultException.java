package org.open.gateway.portal.exception;

import org.open.gateway.portal.constants.ResultCode;

/**
 * Created by miko on 2019/10/29.
 * 响应异常
 *
 * @author MIKO
 */
public class ResultException extends RuntimeException {

    /**
     * 异常代码
     */
    private final ResultCode resultCode;

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

}
