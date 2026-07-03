package com.ewallet;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableCaching
@SpringBootApplication
public class EwalletApplication {
	public static void main(String[] args) {
		SpringApplication.run(EwalletApplication.class, args);
	}
}
