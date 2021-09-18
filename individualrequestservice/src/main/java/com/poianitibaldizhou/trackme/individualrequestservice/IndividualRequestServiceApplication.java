package com.poianitibaldizhou.trackme.individualrequestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IndividualRequestServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IndividualRequestServiceApplication.class, args);
	}
}
