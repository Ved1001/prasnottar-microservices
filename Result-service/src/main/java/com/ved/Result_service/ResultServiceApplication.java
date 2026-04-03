package com.ved.Result_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ResultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultServiceApplication.class, args);
	}

}
