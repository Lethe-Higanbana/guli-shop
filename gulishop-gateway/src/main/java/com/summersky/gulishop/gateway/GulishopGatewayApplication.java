package com.summersky.gulishop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo
 * 1、开启服务的注册与发现
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulishopGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulishopGatewayApplication.class, args);
    }

}
