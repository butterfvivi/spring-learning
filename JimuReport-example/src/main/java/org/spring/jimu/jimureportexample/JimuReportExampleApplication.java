package org.spring.jimu.jimureportexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class JimuReportExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(JimuReportExampleApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.containsProperty("server.servlet.context-path")?env.getProperty("server.servlet.context-path"):"";
    }

}
