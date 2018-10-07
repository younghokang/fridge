package com.poseidon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Application {
    
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
