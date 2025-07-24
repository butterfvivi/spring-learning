### redis

redis.conf
```
port 6379 #指定 Redis 服务器监听的端口号，这是客户端与 Redis 服务器进行通信的端口。
save 900 1#在给定时间间隔内有多少次写操作时，Redis 将执行自动的快照（生成 RDB 文件）。
save 300 10
save 60 10000
dbfilename dump.rdb#指定生成的 RDB 文件的名称。
dir /data #指定持久化文件的存储目录。
appendonly yes #启用 AOF（Append-Only File）持久化模式。
appendfilename "appendonly.aof" #指定 AOF 文件的名称。
appendfsync everysec #控制 AOF 缓冲区的内容何时同步到硬盘。这里的选项 everysec 表示每秒同步一次
cluster-enabled yes #启用 Redis 集群功能。
cluster-config-file nodes.conf #指定保存集群拓扑信息的配置文件名。
cluster-node-timeout 5000 #设置节点间通信的超时时间，单位为毫秒。
```


```shell
#运行 run-conf.sh, 修改配置文件
sh run-conf.sh


# 运行
docker-compose -f docker-compose-redis.yml -p redis up -d


# 构建集群 
# 进入docker 容器中
docker exec -it redis-master bash #redis-master对应的是docker ps -a查看到的容器名redis-master


# 创建集群
redis-cli --cluster create 14.103.123.142:6379 14.103.123.142:6380 192.168.31.164:6379 192.168.31.164:6380 192.168.31.165:6379 192.168.31.165:6380 192.168.31.166:6379 192.168.31.166:6380 --cluster-replicas 1


docker exec -it redis-master redis-cli --cluster create \
  192.168.31.164:6379 192.168.31.164:6380 \
  192.168.31.165:6379 192.168.31.165:6380 \
  192.168.31.166:6379 192.168.31.166:6380 \
  --cluster-replicas 1
  
```