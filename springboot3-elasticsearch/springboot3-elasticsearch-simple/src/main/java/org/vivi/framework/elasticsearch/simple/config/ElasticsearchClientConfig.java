package org.vivi.framework.elasticsearch.simple.config;


import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchClientConfig {

//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        RestClient restClient = RestClient.builder(
//                        new HttpHost("14.103.123.142", 9200, "http"))
//                .build();
//
//        return new ElasticsearchClient(
//                new RestClientTransport(restClient, new JacksonJsonpMapper())
//        );
//    }

//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        RestClient restClient = RestClient.builder(
//                        new HttpHost("14.103.123.142", 9200, "https"))
//                .setHttpClientConfigCallback(httpClientBuilder -> {
//                    // 配置 SSL 上下文以跳过证书验证（开发环境）
//                    try {
//                        SSLContext sslContext = SSLContext.getInstance("TLS");
//                        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
//                            @Override
//                            public X509Certificate[] getAcceptedIssuers() {
//                                return new X509Certificate[0];
//                            }
//
//                            @Override
//                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
//
//                            @Override
//                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
//                        }}, new SecureRandom());
//
//                        httpClientBuilder.setSSLContext(sslContext);
//                        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                    return httpClientBuilder;
//                })
//                .build();
//
//        return new ElasticsearchClient(new RestClientTransport(restClient, new JsonbJsonpMapper()));
//    }

}

