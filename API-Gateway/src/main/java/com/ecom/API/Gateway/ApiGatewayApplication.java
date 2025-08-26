package com.ecom.API.Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {
		"com.ecom.commonRepo.repository"      // agar dao me bhi repo interface hai
})
@EntityScan(basePackages = "com.ecom.CommonEntity")
@SpringBootApplication
@ComponentScan(basePackages = {
		"com.ecom.API.Gateway",
		"com.ecom.commonRepo.repository",
		"com.ecom.commonRepo.dao",
		"com.ecom.CommonEntity"
})
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
