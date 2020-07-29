package org.open.gateway.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by miko on 2020/5/28.
 * 网关路由服务
 *
 * @author MIKO
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayRouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayRouteApplication.class, args);
    }

}
