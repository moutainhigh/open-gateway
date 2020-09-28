package org.open.gateway.portal.configuration;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.*;
import org.open.gateway.portal.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorMappingConfig {

    @ExceptionHandler(ResultException.class)
    public Result convertResultException(ResultException e) {
        ResultCode resultCode = e.getResultCode();
        log.error("Result code: {} msg: {}", resultCode.getCode(), resultCode.getMessage());
        return Result.fail(resultCode);
    }

    @ExceptionHandler(ServiceException.class)
    public Result convertServiceException(ServiceException e) {
        ResultCode resultCode = mappingServiceExceptionToResultCode(e);
        log.error("Result code:{} msg:{}", resultCode.getCode(), resultCode.getMessage());
        return Result.fail(resultCode);
    }

    private ResultCode mappingServiceExceptionToResultCode(ServiceException e) {
        if (e instanceof AccountNotAvailableException) {
            return ResultCode.ACCOUNT_NOT_AVAILABLE;
        }
        if (e instanceof AccountNotExistsException) {
            return ResultCode.ACCOUNT_NOT_EXISTS;
        }
        if (e instanceof AccountPasswordInvalidException) {
            return ResultCode.ACCOUNT_PASSWORD_INVALID;
        }
        if (e instanceof AccountExistsException) {
            return ResultCode.ACCOUNT_IS_EXISTS;
        }
        throw new IllegalStateException("Unmapping exception:" + e.getClass().getName());
    }

}
