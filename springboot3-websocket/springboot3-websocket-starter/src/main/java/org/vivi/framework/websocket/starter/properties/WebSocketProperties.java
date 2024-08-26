package org.vivi.framework.websocket.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("websocket")
public class WebSocketProperties {

    private Boolean enabled;

    private String path;

    private String allowedOrigins;
}
