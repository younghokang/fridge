package com.poseidon;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableHypermediaSupport(type=HypermediaType.HAL)
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class Application {
    
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
    
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Notification-");
        executor.initialize();
        return executor;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}
