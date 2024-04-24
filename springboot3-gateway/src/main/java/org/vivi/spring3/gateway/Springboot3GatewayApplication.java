package org.vivi.spring3.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Springboot3GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot3GatewayApplication.class, args);
    }

}
