package com.ecom.OrderService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EntityScan("com.ecom.CommonEntity")
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("com.ecom.commonRepo.repository")
@ComponentScan(basePackages = {
		"com.ecom.OrderService",
		"com.ecom.commonRepo.repository",
		"com.ecom.commonRepo.dao",
		"com.ecom.CommonEntity"
})
public class OrderServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
