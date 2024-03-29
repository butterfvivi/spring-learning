package org.spring3.knife4j.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("springboot3-knife4j")
                        .version("1.0.0")
                        .description("springboot3-knife4j"));
    }

}
