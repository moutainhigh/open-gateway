package org.open.gateway.portal;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by miko on 2020/6/30.
 *
 * @author MIKO
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "org.open.gateway.portal.persistence.mapper", annotationClass = Mapper.class)
public class GatewayPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayPortalApplication.class, args);
    }

}
