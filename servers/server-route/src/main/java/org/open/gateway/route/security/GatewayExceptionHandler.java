package org.open.gateway.route.security;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Created by miko on 2020/7/9.
 * 异常处理
 *
 * @author MIKO
 */
@Slf4j
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    private final AccessLogsService accessLogsService;

    /**
     * Create a new {@code DefaultErrorWebExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext
            , AccessLogsService accessLogsService
    ) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
        this.accessLogsService = accessLogsService;
    }

    @Override
    protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
        log.error("Error info:{}", throwable.getMessage());
        try {
            this.accessLogsService.sendAccessLogs(request.exchange(), throwable);
        } catch (Exception e) {
            log.error("Send access logs failed:", e);
        }
    }

}
