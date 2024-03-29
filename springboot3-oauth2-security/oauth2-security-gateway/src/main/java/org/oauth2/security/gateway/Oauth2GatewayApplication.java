package org.oauth2.security.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Oauth2GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2GatewayApplication.class,args);
    }
}
