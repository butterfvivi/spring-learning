package org.spring.cloud.consumer.controller;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consumer")
public class ConsumerController {

    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    private final NacosServiceManager nacosServiceManager;

    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    @GetMapping("/")
    public String demo1() {
        return "hello";
    }

    @GetMapping("/getAllProduct")
    public String getAllProduct() {
        return restTemplate.getForObject("http://biz-service/api/biz/findAll", String.class);
    }

    @GetMapping("/getProductProviderInstances")
    public List<ServiceInstance> getProductProviderInstances() {
        return discoveryClient.getInstances("biz-service");
    }

    /**
     * 将该服务从 nacos 注册中心中移除，此时该服务还是可以对外提供服务的
     */
    @GetMapping("/nacosServiceShutDown")
    public void nacosServiceShutDown() throws NacosException {
        nacosServiceManager.nacosServiceShutDown();
    }

    @GetMapping("/requestRoutes")
    public String requestRoutes(String body) {
        return body;
    }
}
