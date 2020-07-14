package com.summersky.gulisop.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Lenovo
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GulishopSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulishopSearchApplication.class, args);
    }

}
