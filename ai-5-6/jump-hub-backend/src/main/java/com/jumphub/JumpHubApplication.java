package com.jumphub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class JumpHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(JumpHubApplication.class, args);
    }
}
