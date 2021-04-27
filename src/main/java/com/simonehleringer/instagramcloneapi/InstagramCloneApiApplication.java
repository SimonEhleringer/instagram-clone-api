package com.simonehleringer.instagramcloneapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication
public class InstagramCloneApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagramCloneApiApplication.class, args);
	}

}
