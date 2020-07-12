package com.summersky.gulishop.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Lenovo
 */
@MapperScan("com.summersky.gulishop.ware.dao")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class GulishopWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulishopWareApplication.class, args);
    }

}
