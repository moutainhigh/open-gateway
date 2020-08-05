package org.open.gateway.route.exception;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

/**
 * Created by miko on 2020/7/9.
 * 异常处理
 *
 * @author MIKO
 */
@Slf4j
@Order(-1) // 在WebFluxResponseStatusExceptionHandler.class之前
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
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);
        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(throwable.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
        return ServerResponse.status(determineHttpStatus(throwable, responseStatusAnnotation))
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(determineMessage(throwable, responseStatusAnnotation)));
    }

    private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }
        if (error instanceof ConnectException) {
            return HttpStatus.GATEWAY_TIMEOUT;
        }
        return responseStatusAnnotation.getValue("code", HttpStatus.class)
                .orElse(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private String determineMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof BindingResult) {
            return error.getMessage();
        }
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getReason();
        }
        String reason = responseStatusAnnotation.getValue("reason", String.class).orElse("");
        if (StringUtils.hasText(reason)) {
            return reason;
        }
        if (error instanceof ConnectException) {
            return HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase();
        }
        return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
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
