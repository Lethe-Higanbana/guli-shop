package com.summersky.gulishop.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.summersky.gulishop.product.dao")
@SpringBootApplication
public class GulishopProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulishopProductApplication.class, args);
    }

}
