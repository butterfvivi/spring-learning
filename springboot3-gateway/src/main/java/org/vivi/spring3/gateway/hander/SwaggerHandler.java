//package org.vivi.spring3.gateway.hander;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.vivi.spring3.gateway.config.MySwaggerResourceProvider;
//import reactor.core.publisher.Mono;
//import springfox.documentation.swagger.web.*;
//
//import java.util.Optional;
//
//
//@RestController
//@RequestMapping("/swagger-resources")
//@RequiredArgsConstructor
//public class SwaggerHandler {
//
//    /**
//     * 权限配置
//     */
//    @Autowired(required = false)
//    private SecurityConfiguration securityConfiguration;
//
//    @Autowired(required = false)
//    private UiConfiguration uiConfiguration;
//
//    //private final SwaggerResourcesProvider swaggerResources;
//
//    private final MySwaggerResourceProvider swaggerResourceProvider;
//
//
//    @GetMapping("/configuration/security")
//    public Mono<ResponseEntity<SecurityConfiguration>> securityConfiguration() {
//        return Mono.just(new ResponseEntity<>(
//                Optional.ofNullable(securityConfiguration).orElse(SecurityConfigurationBuilder.builder().build()), HttpStatus.OK));
//    }
//
//    @GetMapping("/configuration/ui")
//    public Mono<ResponseEntity<UiConfiguration>> uiConfiguration() {
//        return Mono.just(new ResponseEntity<>(
//                Optional.ofNullable(uiConfiguration).orElse(UiConfigurationBuilder.builder().build()), HttpStatus.OK));
//    }
//
//    /**
//     * 获取接口信息
//     */
//    @GetMapping("")
//    public Mono<ResponseEntity> swaggerResources() {
//        return Mono.just((new ResponseEntity<>(swaggerResourceProvider.get(), HttpStatus.OK)));
//    }
//}
//
