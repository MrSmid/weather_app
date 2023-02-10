package com.evolenta.personmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PersonMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonMicroserviceApplication.class, args);
    }

}
