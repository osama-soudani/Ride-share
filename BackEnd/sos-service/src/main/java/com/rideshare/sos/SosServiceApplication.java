package com.rideshare.sos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SosServiceApplication.class, args);
	}

}
