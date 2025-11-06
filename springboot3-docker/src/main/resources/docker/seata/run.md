### Seata - 分布式事务

```shell

# 修改seata配置文件`./seata-server/resources/application.yml`
# 修改`docker-compose-seata.yml`相关IP配置

# nacos命名空间`prod`下新建配置`seata-server.properties`
# 新建数据库`seata-server`，导入sql脚本`./sql/seata-server.sql`

# 运行
docker-compose -f docker-compose-seata.yml -p seata up -d
# 进入容器
docker exec -it seata-server sh
# 查看日志
docker logs -f seata-server
  

```

访问地址：[`http://14.103.123.142:7091/nacos`](http://www.zhengqingya.com:8848/nacos)
登录账号密码默认：`seata/seata`

