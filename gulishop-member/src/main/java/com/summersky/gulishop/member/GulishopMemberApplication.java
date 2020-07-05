package com.summersky.gulishop.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.summersky.gulishop.member.feign")
@SpringBootApplication
public class GulishopMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulishopMemberApplication.class, args);
    }

}
