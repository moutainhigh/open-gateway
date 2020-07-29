package org.open.gateway.route.exception;

import lombok.extern.slf4j.Slf4j;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.*;

/**
 * Created by miko on 2020/7/9.
 * 异常处理
 *
 * @author MIKO
 */
@Slf4j
public class WebExceptionHandler implements ErrorWebExceptionHandler {

    private static final String ERROR_ATTRIBUTE = WebExceptionHandler.class.getName() + ".ERROR";

    /**
     * Currently duplicated from Spring WebFlux HttpWebHandlerAdapter.
     */
    private static final Set<String> DISCONNECTED_CLIENT_EXCEPTIONS;

    static {
        Set<String> exceptions = new HashSet<>();
        exceptions.add("AbortedException");
        exceptions.add("ClientAbortException");
        exceptions.add("EOFException");
        exceptions.add("EofException");
        DISCONNECTED_CLIENT_EXCEPTIONS = Collections.unmodifiableSet(exceptions);
    }

    private final AccessLogsService accessLogService;

    public WebExceptionHandler(AccessLogsService accessLogService) {
        this.accessLogService = accessLogService;
    }

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable error) {
        //错误记录
        ServerHttpRequest request = exchange.getRequest();
        log.error("Exception request path:{}, error msg:{}", request.getPath(), error.getMessage());
        //参考AbstractErrorWebExceptionHandler
        if (exchange.getResponse().isCommitted() || isDisconnectedClientError(error)) {
            return Mono.error(error);
        }
        // 保存错误信息
        storeErrorInformation(error, exchange);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
                .route(newRequest)
                .switchIfEmpty(Mono.error(error))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response))
                .then(this.accessLogService.sendAccessLogs(exchange, error));

    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        // 按照异常类型进行处理
        if (error instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) error;
            return buildServerResponse(responseStatusException.getStatus(), Optional.ofNullable(responseStatusException.getReason()).orElse(responseStatusException.getMessage()));
        }
        // 处理带responseStatus注解
        ResponseStatus responseStatusAnnotation = error.getClass().getDeclaredAnnotation(ResponseStatus.class);
        if (responseStatusAnnotation != null) {
            return buildServerResponse(responseStatusAnnotation.code(), responseStatusAnnotation.reason());
        }
        if (error instanceof TokenExpiredException) {
            return buildServerResponse(HttpStatus.UNAUTHORIZED, error.getMessage());
        }
        if (error instanceof InvalidTokenException) {
            return buildServerResponse(HttpStatus.UNAUTHORIZED, error.getMessage());
        }
        if (error instanceof ConnectException) {
            return buildServerResponse(HttpStatus.SERVICE_UNAVAILABLE, "Request path: " + request.path() + " service unavailable");
        }
        if (error instanceof InvalidClientSecretException) {
            return buildServerResponse(HttpStatus.FORBIDDEN, error.getMessage());
        }
        if (error instanceof NoClientFoundException) {
            return buildServerResponse(HttpStatus.FORBIDDEN, error.getMessage());
        }
        log.error(error.getMessage(), error);
        return buildServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage());
    }

    protected Mono<ServerResponse> buildServerResponse(HttpStatus httpStatus, String body) {
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body));
    }

    protected Throwable getError(ServerRequest request) {
        return (Throwable) request.attribute(ERROR_ATTRIBUTE)
                .orElseThrow(() -> new IllegalStateException("Missing exception attribute in ServerWebExchange"));
    }

    protected void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
        exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE, error);
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    private boolean isDisconnectedClientError(Throwable ex) {
        return DISCONNECTED_CLIENT_EXCEPTIONS.contains(ex.getClass().getSimpleName())
                || isDisconnectedClientErrorMessage(NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
    }

    private boolean isDisconnectedClientErrorMessage(String message) {
        message = (message != null) ? message.toLowerCase() : "";
        return (message.contains("broken pipe") || message.contains("connection reset by peer"));
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return WebExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return WebExceptionHandler.this.viewResolvers;
        }

    }

}
