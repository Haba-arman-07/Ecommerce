package com.ecom.ProductService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.ecom.CommonEntity")
@EnableCaching
@SpringBootApplication
@EnableJpaRepositories("com.ecom.commonRepo.repository")
@ComponentScan(basePackages = {
		"com.ecom.ProductService",
		"com.ecom.commonRepo.repository",
		"com.ecom.commonRepo.dao",
		"com.ecom.CommonEntity"
})
public class ProductApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ProductApplication.class, args);
	}

}
