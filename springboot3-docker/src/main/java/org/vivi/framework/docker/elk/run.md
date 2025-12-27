### ELK

Elasticsearch + Logstash + Kibana + Filebeat 搭建日志监控系统

```
1、Filebeat 采集日志
2、Logstash 日志过滤
3、Elasticsearch 日志搜索
4、Kibana 日志展示
```

### 1、安装启动docker容器

```shell
# 运行
docker-compose -f docker-compose-elk.yml -p elk up -d 

# 运行某个service
docker-compose -f docker-compose-elk.yml up -d elasticsearch
# 若运行之后启动日志报相关权限问题，给新产生的文件赋予权限
chmod -R 777 ./elk
```

1. ES访问地址：[`ip地址:9200`](http://www.zhengqingya.com:9200)
   默认账号密码：`elastic/123456`
2. kibana访问地址：[`ip地址:5601`](http://www.zhengqingya.com:5601)
   默认账号密码：`elastic/123456`

### 2、设置ES密码,

```shell
# 进入 ElasticSearch 容器，配置账号供 Kibana 使用
docker exec -it elk_elasticsearch /bin/bash
# 设置密码-随机生成密码
# elasticsearch-setup-passwords auto
# 设置密码-手动设置密码
#elasticsearch-setup-passwords interactive

#创建新账户
elasticsearch-users useradd root

#给账户授权
elasticsearch-users roles -a superuser root
elasticsearch-users roles -a kibana_system root


```

### 3、配置kibana账号

```shell
# 进入 ElasticSearch 容器
docker exec -it elk_elasticsearch /bin/bash

# 拿到es token
bin/elasticsearch-create-enrollment-token --scope kibana

# 进入kibana容器
docker exec -it elk_kibana /bin/bash
bin/kibana-verification-code 

# 配置后可进入页面

# 获取kibana code
```


### 4、IK 分词器
1. 下载ik分词器
* https://release.infinilabs.com/analysis-ik/stable/

2. 配置ik分词器
```shell

# 进入 ElasticSearch 容器
docker exec -it elk_elasticsearch /bin/bash

#在线安装
./bin/elasticsearch-plugin install https://release.infinilabs.com/analysis-ik/stable/elasticsearch-analysis-ik-8.17.2.zip

#或者通过本地安装
./bin/elasticsearch-plugin install  /YOUR_PATH/elasticsearch-analysis-ik-8.17.2.zip

# 注意！安装时不会自动导入配置文件，需要手动把压缩包内的文件夹 /config 复制到 /usr/share/elasticsearch/plugins/analysis-ik/ 下

# 重启容器，完成安装
docker restart elastic_search_container 

# 进入 ElasticSearch 容器 ，列出已安装的插件
cd bin
elasticsearch-plugin list


```