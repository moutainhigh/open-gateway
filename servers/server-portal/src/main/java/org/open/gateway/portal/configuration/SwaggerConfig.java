package org.open.gateway.portal.configuration;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.open.gateway.portal.security.AccountDetails;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.service.contexts.SecurityContextBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by miko on 11/3/20.
 *
 * @author MIKO
 */
@AllArgsConstructor
@Configuration
public class SwaggerConfig {

    private final ServerProperties serverProperties;
    private final PortalSecurityProperties securityProperties;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .apiInfo(getApiInfo())
                .ignoredParameterTypes(AccountDetails.class)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .securityContexts(getSecurityContexts())
                .securitySchemes(getSecuritySchemes())
                .groupName("default");
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("能力开放平台管理后台接口文档")
                .description("")
                .version("V1.0.0")
                .build();
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> getSecuritySchemes() {
        return Arrays.asList(
                new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header")
        );
    }

    private List<SecurityContext> getSecurityContexts() {
        Set<String> whitePaths = Stream.of(securityProperties.getPermitAll())
                .map(url -> serverProperties.getServlet().getContextPath() + url)
                .collect(Collectors.toSet());

        return Arrays.asList(
                new SecurityContextBuilder()
                        .securityReferences(defaultAuth())
                        .operationSelector(oc -> !whitePaths.contains(oc.requestMappingPattern()))
                        .build()
        );
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes));
        return securityReferences;
    }

}
