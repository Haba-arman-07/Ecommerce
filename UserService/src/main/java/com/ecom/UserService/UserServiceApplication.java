package com.ecom.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EntityScan("com.ecom.CommonEntity")
@EnableJpaRepositories("com.ecom.commonRepo.repository")
@ComponentScan(basePackages = {
		"com.ecom.UserService",
		"com.ecom.commonRepo.repository",
		"com.ecom.commonRepo.dao",
		"com.ecom.CommonEntity"
		})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
