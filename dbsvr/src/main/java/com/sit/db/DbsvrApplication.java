package com.sit.db;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("mapper")//将项目中对应的mapper类的路径加进来就可以了
public class DbsvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbsvrApplication.class, args);
	}
}
