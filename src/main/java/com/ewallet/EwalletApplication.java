package com.ewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
public class EwalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(EwalletApplication.class, args);
    }
}
