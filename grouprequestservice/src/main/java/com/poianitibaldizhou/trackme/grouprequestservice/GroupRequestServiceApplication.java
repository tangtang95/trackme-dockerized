package com.poianitibaldizhou.trackme.grouprequestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GroupRequestServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GroupRequestServiceApplication.class, args);
	}
}
