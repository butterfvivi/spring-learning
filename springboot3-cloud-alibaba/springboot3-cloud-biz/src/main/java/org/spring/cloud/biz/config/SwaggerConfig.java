//package org.spring.cloud.biz.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import org.springframework.context.annotation.Configuration;
//
//import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.annotation.Order;
//
//@Configuration
//@EnableKnife4j
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI springOpenAPI() {
//        return new OpenAPI().info(new Info() //
//                .title("SpringDoc API Test") //
//                .description("SpringDoc Simple Application Test") //
//                .version("0.0.1"));
//    }
//
//
////    @Bean
////    @Order(value = 1)
////    public OpenAPI groupRestApi() {
////        return new OpenAPI(DocumentationType.SWAGGER_2)
////                .info(groupApiInfo())
////                .select()
////                .apis(RequestHandlerSelectors.basePackage("org.spring.cloud.biz.controller"))
////                .paths(PathSelectors.any())
////                .build();
////    }
////
////    private ApiInfo groupApiInfo() {
////        return new ApiInfoBuilder()
////                .title("Biz接口文档")
////                .description("<div style='font-size:14px;color:red;'>Biz</div>")
////                .termsOfServiceUrl("http://localhost:3002/api/")
////                .contact(new Contact("Vivi","https://github.com/",""))
////                .version("1.0")
////                .build();
////    }
//
//}
