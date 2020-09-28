package org.open.gateway.portal.configuration;

import lombok.extern.slf4j.Slf4j;
import open.gateway.common.utils.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class AutoLoggingAspect {

    @Value("${autoLogging.enableGet:false}")
    private boolean enableGet;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && !target(org.open.gateway.portal.configuration.ErrorMappingConfig)")
    private void anyRestController() {

    }

    @Around("anyRestController()")
    private Object logAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed(joinPoint.getArgs());
            long endTime = System.currentTimeMillis();
            String requestLog = buildRequestLog(joinPoint, result, endTime - startTime);
            if (StringUtils.hasText(requestLog)) {
                logger.info(requestLog);
            }
            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            logger.info(buildRequestLog(joinPoint, "exception: " + e.getMessage(), endTime - startTime));
            throw e;
        }
    }

    private String buildRequestLog(ProceedingJoinPoint joinPoint, Object response, long duration) {
        try {
            Method method = getJoinPointMethod(joinPoint);
            if (method == null || !shouldAutoLogging(method)) {
                return null;
            }
            String path = extractEndPoint(method);
            String userId = extractUserId(joinPoint, method);
            String responseStr;
            if (response instanceof ResponseEntity) {
                Object body = ((ResponseEntity<?>) response).getBody();
                responseStr = JSON.toJSONString(body);
            } else {
                responseStr = JSON.toJSONString(response);
            }
            return "===> Path: " + path + " | " +
                    "User: " + userId + " | " +
                    "Duration: " + duration + "ms | " +
                    "Params: " + extractRequestParams(joinPoint, method) + " | " +
                    "Request: " + extractRequestBody(joinPoint, method) + " | " +
                    "Response: " + responseStr;
        } catch (Exception e) {
            log.error("[buildRequestLog] exception: ", e);
            return null;
        }

    }

    private boolean shouldAutoLogging(Method method) {
        Annotation[] annotations = method.getAnnotations();
        boolean result = true;
        for (Annotation item : annotations) {
            if (item instanceof GetMapping) {
                result = enableGet;
                break;
            } else if (item instanceof RequestMapping) {
                RequestMapping mapping = (RequestMapping) item;
                if (mapping.method().length > 0) {
                    RequestMethod requestMethod = mapping.method()[0];
                    if (requestMethod == RequestMethod.GET) {
                        result = enableGet;
                    }
                    break;
                }
            }
        }
        return result;
    }

    private String extractEndPoint(Method method) {
        Annotation[] annotations = method.getAnnotations();
        String result = null;
        for (Annotation item : annotations) {
            if (item instanceof PostMapping) {
                result = ((PostMapping) item).value()[0];
            } else if (item instanceof DeleteMapping) {
                result = ((DeleteMapping) item).value()[0];
            } else if (item instanceof PutMapping) {
                result = ((PutMapping) item).value()[0];
            } else if (item instanceof RequestMapping) {
                RequestMapping mapping = (RequestMapping) item;
                if (mapping.method().length > 0) {
                    RequestMethod requestMethod = mapping.method()[0];
                    switch (requestMethod) {
                        case POST:
                        case PUT:
                        case DELETE:
                            result = mapping.value()[0];
                            break;
                    }
                }
            }
        }
        return result;
    }

    private String extractUserId(ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        int paramIndex = 0;
        for (Annotation[] paramAnnotations : annotations) {
            for (Annotation item : paramAnnotations) {
                if (item.annotationType() == AuthenticationPrincipal.class) {
                    if (args[paramIndex] instanceof CharSequence) {
                        return args[paramIndex].toString();
                    } else {
                        return JSON.toJSONString(args[paramIndex]);
                    }
                }
            }
            paramIndex++;
        }
        return null;
    }

    private String extractRequestBody(ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        int paramIndex = 0;
        for (Annotation[] paramAnnotations : annotations) {
            for (Annotation item : paramAnnotations) {
                if (item.annotationType() == RequestBody.class) {
                    return JSON.toJSONString(args[paramIndex]);
                }
            }
            paramIndex++;
        }
        return null;
    }


    private String extractRequestParams(ProceedingJoinPoint joinPoint, Method method) {
        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        StringBuilder builder = new StringBuilder();
        int paramIndex = 0;
        for (Annotation[] paramAnnotations : annotations) {
            for (Annotation item : paramAnnotations) {
                if (item.annotationType() == RequestParam.class) {
                    RequestParam requestParamAnnotation = (RequestParam) item;
                    String paramName = requestParamAnnotation.value();
                    String value = String.valueOf(args[paramIndex]);
                    builder.append(String.format("%s=%s", paramName, value));
                    builder.append('&');
                }
            }
            paramIndex++;
        }
        return builder.toString();
    }

    private Method getJoinPointMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Class<?> clazz = joinPoint.getSignature().getDeclaringType();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        log.warn("Can not found join point method");
        return null;
    }


}
