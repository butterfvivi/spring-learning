## Springboot3 集成 Sharding-JDBC

### docker-compose 安装部署 mysql 主从复制架构，一主二从

参考 Springboot3-docker 中的 mysql 下 mysql-nodes 目录

* [sharding-jdbc](https://github.com/apache/shardingsphere/tree/master/sharding-jdbc)

⚠️ springboot3 集成 shardingsphere-JDBC5.5.2 与 springboot2 不同，不再提供 springboot-starter-shardingsphere， 相关配置也采用了独立的配置文件

### 问题
- 主从数据不一致问题
  业务解决，订单下单后给一个订单完成的中间页面，而不是直接给出返回结果，留出API的处理时间