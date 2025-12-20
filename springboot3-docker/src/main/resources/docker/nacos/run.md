### Nacos

```shell


docker run -d \
  -e MODE=standalone \
  -e NACOS_AUTH_ENABLE=true \
  -e NACOS_AUTH_TOKEN_SECRET_KEY=8YwP0JNJpD7O+VZDIh69iQ== \
  -e SPRING_DATASOURCE_PLATFORM=mysql \
  -e MYSQL_SERVICE_HOST=你的MySQL地址 \
  -e MYSQL_SERVICE_PORT=3306 \
  -e MYSQL_SERVICE_USER=root \
  -e MYSQL_SERVICE_PASSWORD=Wtf0010. \
  -e MYSQL_SERVICE_DB_NAME=nacos_config \
  -p 8848:8848 \
  nacos/nacos-server:v2.4.0
  
  
# 普通单机模式版本  注：需要修改docker-compose-docker-compose-sentinel-mysql.yml 中相关数据库连接信息和JVM参数相关信息
docker-compose -f docker-compose-nacos.yml -p nacos up -d
```

访问地址：[`http://14.103.123.142:8848/nacos`](http://www.zhengqingya.com:8848/nacos)
登录账号密码默认：`nacos/nacos`

