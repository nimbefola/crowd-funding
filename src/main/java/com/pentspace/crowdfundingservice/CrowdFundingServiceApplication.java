package com.pentspace.crowdfundingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CrowdFundingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrowdFundingServiceApplication.class, args);
	}

}
