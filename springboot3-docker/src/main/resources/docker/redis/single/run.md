### redis


```shell
# 运行
docker-compose -f docker-compose-redis.yml -p redis up -d


验证是否部署成功
docker exec -it redis-node1 bash
redis-cli -p 6381
auth 123456
cluster info

```