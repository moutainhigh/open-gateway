package org.open.gateway.route.exception;

import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Created by miko on 2020/7/9.
 * 异常处理
 * 在WebFluxResponseStatusExceptionHandler.class之前执行
 *
 * @author MIKO
 */
@Slf4j
@Order(-1)
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    /**
     * Create a new {@code GatewayExceptionHandler} instance.
     *
     * @param errorAttributes    the error attributes
     * @param resourceProperties the resources configuration properties
     * @param errorProperties    the error configuration properties
     * @param applicationContext the current application context
     */
    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable throwable = getError(request);
        HttpStatus status;
        String message;
        if (throwable instanceof ResponseStatusException) {
            status = ((ResponseStatusException) throwable).getStatus();
            message = ((ResponseStatusException) throwable).getReason();
        } else if (throwable instanceof ConnectTimeoutException) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
        } else {
            MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(throwable.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
            status = responseStatusAnnotation.getValue("code", HttpStatus.class)
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
            message = responseStatusAnnotation.getValue("reason", String.class)
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        return ServerResponse.status(status)
                .contentType(MediaType.TEXT_PLAIN)
                .body(BodyInserters.fromValue(Optional.ofNullable(message)));
    }

    private HttpStatus determineHttpStatus(Throwable throwable) {
        if (throwable instanceof ResponseStatusException) {
            return ((ResponseStatusException) throwable).getStatus();
        }
        if (throwable instanceof ConnectTimeoutException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(throwable.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
        return responseStatusAnnotation.getValue("code", HttpStatus.class)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String determineMessage(Throwable error) {
        if (error instanceof BindingResult) {
            return error.getMessage();
        }
        return null;
    }

    @Override
    protected void logError(ServerRequest request, ServerResponse response, Throwable throwable) {
        log.error("Request path[{}] error: {}", request.path(), throwable.getMessage());
    }

}
