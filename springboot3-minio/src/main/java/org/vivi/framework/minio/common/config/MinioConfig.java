package org.vivi.framework.minio.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.vivi.framework.minio.common.MinioProperties;

/**
 * aws-s3 通用存储操作 支持所有兼容s3协议的云存储: 阿里云OSS、腾讯云COS、华为云、七牛云、，京东云、minio
 * @author weimeilayer@gmail.com
 * @date 2021年2月3日
 */
@Configuration
@AllArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
        return minioClient;
    }
}

