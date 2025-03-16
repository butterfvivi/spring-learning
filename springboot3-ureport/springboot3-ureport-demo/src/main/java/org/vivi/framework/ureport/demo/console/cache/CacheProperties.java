package org.vivi.framework.ureport.demo.console.cache;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.ureport.demo.core.utils.ReportProperties;

@Component
public class CacheProperties {

    @Autowired
    private ReportProperties reportProperties;

    private static boolean enableRedis;

    private static int cacheExpire;

    public static boolean isEnableRedis() {
        return enableRedis;
    }

    public static void setEnableRedis(boolean enableRedis) {
        CacheProperties.enableRedis = enableRedis;
    }

    public static int getCacheExpire() {
        return cacheExpire;
    }

    public static void setCacheExpire(int cacheExpire) {
        CacheProperties.cacheExpire = cacheExpire;
    }

    @PostConstruct
    public void init() {
        CacheProperties.enableRedis = reportProperties.isEnableRedis();
        CacheProperties.cacheExpire = reportProperties.getCacheExpire();
    }

}
