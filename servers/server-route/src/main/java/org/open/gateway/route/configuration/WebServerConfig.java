package org.open.gateway.route.configuration;

import org.open.gateway.route.exception.WebExceptionHandler;
import org.open.gateway.route.service.AccessLogsService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

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
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer,
                                                             AccessLogsService accessLogService) {
        WebExceptionHandler exceptionHandler = new WebExceptionHandler(accessLogService);
        exceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

}
