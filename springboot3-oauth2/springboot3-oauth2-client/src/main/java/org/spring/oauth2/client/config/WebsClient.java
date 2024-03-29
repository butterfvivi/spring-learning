package org.spring.oauth2.client.config;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:2001")
public interface WebsClient {

    @GetExchange("/")
    String getClient();

}
