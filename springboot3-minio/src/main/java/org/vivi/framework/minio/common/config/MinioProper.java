package org.vivi.framework.minio.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.minio.common.utils.MinIOUtils1;

@Configuration
@Data
public class MinioProper {


    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.fileHost}")
    private String fileHost;
    @Value("${minio.bucket-name}")
    private String bucketName;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinIOUtils1 creatMinioClient() {

        return new MinIOUtils1(endpoint, fileHost, bucketName, accessKey, secretKey);
    }
}
