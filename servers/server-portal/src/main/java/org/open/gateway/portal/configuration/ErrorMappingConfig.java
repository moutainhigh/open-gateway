package org.open.gateway.portal.configuration;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.portal.constants.ResultCode;
import org.open.gateway.portal.exception.*;
import org.open.gateway.portal.vo.Result;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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
        log.error("Service exception msg:{}", e.getMessage());
        ResultCode resultCode = mappingServiceExceptionToResultCode(e);
        log.error("Result code:{} msg:{}", resultCode.getCode(), resultCode.getMessage());
        return Result.fail(resultCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String convertMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> objectErrorList = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError objectError : objectErrorList) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                sb.append(fieldError.getField());
                sb.append(":");
                sb.append(fieldError.getDefaultMessage());
            } else {
                sb.append(objectError.getDefaultMessage());
            }
            sb.append("; ");
        }
        log.error("Valid result is:{}", sb.toString());
        return sb.toString();
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
        if (e instanceof AccountAlreadyExistsException) {
            return ResultCode.ACCOUNT_IS_EXISTS;
        }
        throw new IllegalStateException("Unmapping exception:" + e.getClass().getName());
    }

}
