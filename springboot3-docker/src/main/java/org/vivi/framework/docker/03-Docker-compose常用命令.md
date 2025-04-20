# 常用命令

### 常用命令

```shell
# 启动容器
docker-compose up

#如果希望在后台运行容器，可以使用 -d
docker-compose up -d

#启动某一个容器
docker-compose up -d mysql(service)

#查看容器状态
docker-compose ps

#停止容器
docker-compose stop 命令用于停止正在运行的容器，可以通过 docker-compose start 再次启动。
docker-compose stop

#重新启动容器

docker-compose restart 命令用于重新启动由 Docker Compose 启动的服务容器。
docker-compose restart
#删除容器

docker-compose rm 命令用于删除所有（停止状态的）服务容器。
docker-compose rm

#执行命令

docker-compose run 命令用于在指定服务上执行一个命令。
docker-compose run <service> <command>

```


