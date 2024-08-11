# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

1、oauth2-client-simple：
github做授权服务器

step1
get github support : https://github.com/settings/applications/new


- IntelliJ IDEA 
- spring boot: 3.1  
- jdk: 17+  
- maven: 3.5.4+


springboot3-learning # 父级工程
- JimuReport-example # 整合jimu
- springboot3-cloud-alibaba # 集成springcloud Alibaba
- springboot3-dynamic-datasource # 整合多数据源(Dynamic DataSource)
- springboot3-gateway
    - 1. gateway 的一些基本用法
    - 2. spring.cloud.gateway.enabled 用来配置是否启动网关
    - 3. spring.cloud.gateway.routes[index].uri=[http|lb] lb表示负载均衡的地址 eg: lb:consumer-service
    - 4. route predicate factory 的使用
    - 5. 自己编写一个 Route Predicate Factory 
- springboot3-iexcel # 使用easypoi/easyexcel 解析或者导出excel
    - 1. springboot3-easyexcel-simple # springboot3集成easyexcel 简单示例
    - 2. springboot3-iexcel-batch # springboot3集成easyexcel 实现大数据库异步导入 CompletableFuture/Future
    - 3. springboot3-iexcel-toolkit # springboot3集成easyexcel 导入导出注解封装
- springboot3-knife4j # 整合knife4j
- springboot3-minio # springboot3 集成minio docker搭建mino 实现文档上传
- springboot3-mybatisplus-druid # 整合mybatisplus
- springboot3-oauth2  # SpringBoot3 + security6 + Spring Cloud Gateway 整合 Oauth2 实现认证操作
  - springboot3-oauth2-client
    - 1. client服务，对外提供接口
    - 2. 获取网关服务传递下来的token信息，也可以传递具体的认证数据
  - springboot3-oauth2-resource-server
    - 1. 实现网关的认证
    - 2. 向下游服务的请求头中传递解码后的token数据
  - oauth2-client-simple
    - 1. 简单认证
- springboot3-oauth2-security  # oauth2 整合 security
    - 1. oauth2-security-server # 认证服务器
    - 2. oauth2-security-gateway # 网关
    - 3. oauth2-security-client # 客户端
- springboot3-postgresql # springboot3集成postgresql数据库
- springboot3-redis # springboot3集成redis ，实现redis存放各种类型数据
- springboot3-sa-token # springboot3集成sa-token，实现登录认证
- springboot3-security-jwt # springboot3集成security，jwt生成token
- springboot3-webflux # webflux
- springboot3-websocket # 整合websocket
- ureport-example # 整合ureport



![img.png](spring-oauth2/oauth2-client-simple/img.png)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.0/maven-plugin/reference/html/#build-image)

