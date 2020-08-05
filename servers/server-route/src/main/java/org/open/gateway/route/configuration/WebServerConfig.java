package org.open.gateway.route.configuration;

import org.open.gateway.route.exception.GatewayExceptionHandler;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.stream.Collectors;

/**
 * Created by miko on 2020/7/8.
 * web配置
 *
 * @author MIKO
 */
@Configuration
public class WebServerConfig {

//    /**
//     * 配置负载均衡web客户端
//     */
//    @Bean
//    public WebClient webclient(ReactorLoadBalancerExchangeFilterFunction lbFunction) {
//        return WebClient.builder().filter(lbFunction).build();
//    }

    /**
     * 配置统一异常处理类
     */
    @Bean
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
                                                             ResourceProperties resourceProperties, ObjectProvider<ViewResolver> viewResolvers,
                                                             ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext,
                                                             ServerProperties serverProperties,
                                                             AccessLogsService accessLogService) {
        GatewayExceptionHandler exceptionHandler = new GatewayExceptionHandler(errorAttributes, resourceProperties, serverProperties.getError(), applicationContext, accessLogService);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

}
