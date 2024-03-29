package org.oauth2.security.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SecurityClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityClientApplication.class,args);
    }
}
