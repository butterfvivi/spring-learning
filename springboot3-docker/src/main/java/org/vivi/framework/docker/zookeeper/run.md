### redis

创建macvlan网络

```shell
docker network create -d macvlan --subnet=10.0.3.0/24 --gateway=10.0.3.1 -o parent=em2 zookeeper-kafka-net
```


```shell
# 运行
docker-compose -f docker-compose-zk.yml -p zk up -d

```