package org.vivi.framework.minio.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * aws 配置信息bucket 设置公共读权限
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 对象存储服务的URL
     */
    @Schema(description = "对象存储服务的URL")
    private String endpoint;

    /**
     * 自定义域名
     */
    @Schema(description = "自定义域名")
    private String customDomain;

    /**
     * 反向代理和S3默认支持
     */
    @Schema(description = "反向代理和S3默认支持")
    private Boolean pathStyleAccess = true;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private String appId;

    /**
     * 区域
     */
    @Schema(description = "区域")
    private String region;

    /**
     * 预览地址
     */
    @Schema(description = "预览地址")
    private String previewDomain;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    @Schema(description = "Access key就像用户ID，可以唯一标识你的账户")
    private String accessKey;

    /**
     * Secret key是你账户的密码
     */
    @Schema(description = "Secret key是你账户的密码")
    private String secretKey;

    /**
     * 默认的存储桶名称
     */
    @Schema(description = "默认的存储桶名称")
    private String bucketName;
    /**
     * 公开桶名
     */
    @Schema(description = "公开桶名")
    private String publicBucketName;

    /**
     * 物理删除文件
     */
    @Schema(description = "物理删除文件")
    private boolean physicsDelete;

    /**
     * 最大线程数，默认： 100
     */
    @Schema(description = "最大线程数，默认： 100")
    private Integer maxConnections = 100;

    /**
     * 默认是秒 地址过期时间，设置默认值7200秒
     */
    private int expire = 7200;
}


