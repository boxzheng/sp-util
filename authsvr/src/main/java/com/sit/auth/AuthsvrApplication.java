package com.sit.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
@EnableFeignClients(basePackages = {"com.sit.client"})
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan
public class AuthsvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthsvrApplication.class, args);
	}
}
