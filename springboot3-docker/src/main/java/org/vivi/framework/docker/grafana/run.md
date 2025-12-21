### Grafana - 开源数据可视化工具(数据监控、数据统计、警报)

数据目录 请执行 `chmod 777 /docker/grafana` 赋予读写权限 否则将无法写入数据

```shell

docker-compose -f docker-compose-grafana.yml -p grafana up -d

```

访问地址：[`http://ip地址:3000`](http://www.zhengqingya.com:3000)
默认登录账号密码：`admin/admin`

#访问Prometheus server主页
http://ip:9090

#访问Prometheus server自身指标
http://ip:9090/metrics

#访问Grafana
http://主节点IP地址:3000 
