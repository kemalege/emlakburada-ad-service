package com.patika.emlakburadaadservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmlakburadaAdServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmlakburadaAdServiceApplication.class, args);
    }

}
