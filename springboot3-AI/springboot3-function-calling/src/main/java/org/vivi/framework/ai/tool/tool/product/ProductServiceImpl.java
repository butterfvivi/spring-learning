package org.vivi.framework.ai.tool.tool.product;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.client.RestTemplate;



public class ProductServiceImpl implements ProductService {

    private  RestTemplate restTemplate  = new RestTemplate();

    @Tool(description = "根据商品ID获取商品的详细信息")
    @Override
    public Product getProductById(String productId) {
        String url = "http://localhost:8082/api/products/{productId}";
        return restTemplate.getForObject(url, Product.class, productId);

    }

}