package org.spring.oauth2.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "org.springframework.security.oauth2.jwt.JwtDecoder")
public class OauthResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthResourceApplication.class, args);
    }

}
