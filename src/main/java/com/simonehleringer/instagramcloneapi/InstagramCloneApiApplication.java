package com.simonehleringer.instagramcloneapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InstagramCloneApiApplication {

	// TODO: Reorganize folder structure of whole project
	public static void main(String[] args) {
		SpringApplication.run(InstagramCloneApiApplication.class, args);
	}
}
