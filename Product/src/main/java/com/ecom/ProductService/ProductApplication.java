package com.ecom.ProductService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.ecom.CommonEntity")
@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ProductApplication.class, args);
	}

}
