package com.leaptour.leaptourbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LeapTourBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeapTourBackendApplication.class, args);
	}

}
