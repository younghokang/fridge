package com.poseidon.fridge.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FridgeMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridgeMemberApplication.class, args);
	}
	
}
