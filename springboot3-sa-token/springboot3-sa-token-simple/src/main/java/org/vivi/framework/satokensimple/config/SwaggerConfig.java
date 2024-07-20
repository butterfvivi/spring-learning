package org.vivi.framework.satokensimple.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("sa-token登录管理")
                .description("使用springboot3")
                .version("v1.0.0")
                .license(new License().name("Apache 2.0").url("https://springdoc.org")));
        openAPI.externalDocs(new ExternalDocumentation().description("项目API文档")
                .url("/"));
        return openAPI;
    }
}
