package com.project.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    })
public class AuthenticatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticatorServiceApplication.class, args);
	}

}
