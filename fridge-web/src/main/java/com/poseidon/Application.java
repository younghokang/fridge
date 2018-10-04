package com.poseidon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
    
    @LoadBalanced
    @Bean
    public RestTemplate fridgeServiceRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://fridge-service").build();
    }
    
    @LoadBalanced
    @Bean
    public RestTemplate memberRestTemplate(RestTemplateBuilder builder) {
        return builder.rootUri("http://fridge-member").build();
    }
    
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
