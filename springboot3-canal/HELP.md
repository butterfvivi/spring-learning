# Getting Started


## 1、canal

canal可做数据同步
canal的数据同步不是全量的，而是增量。基于binary log增量订阅和消费，canal可以做：

- 数据库镜像
- 数据库实时备份
- 索引构建和实时维护
- 业务cache(缓存)刷新
- 带业务逻辑的增量数据处理

[canal 官方文档](https://canal.mydoc.io/)

## 2、docker-compose 安装部署canal

参考 Springboot3-docker 中的 canal 目录

查看是否打开binlog模式

```shell
show variables like '%log_bin%';
```
查看binlog日志文件列表
```shell
show master logs;
```

查看当前正在写入的binlog文件
```shell
show master status;
```

## 3、Springboot3+ 整合canal

完成实例代码

#### 测试canal订阅binlog

数据库创建一个测试表，并插入一条数据
```shell

CREATE TABLE `tb_canal_test` (
  `id` varchar(32) NOT NULL,
  `name` varchar(512) DEFAULT NULL COMMENT '商品名称',
  `price` varchar(36) DEFAULT '0' COMMENT '商品价格',
  `number` int(10) DEFAULT '0' COMMENT '商品数量',
  `description` varchar(2048) DEFAULT '' COMMENT '商品描述',
  PRIMARY KEY (`id`)
);

INSERT INTO tb_canal_test VALUES('3e71a81fd80711eaaed600163e046cc3','叉烧包','3.99',3,'又大又香的叉烧包');
```

控制台打印：
```shell

================&gt; binlog[mysql-bin.000003:25172] , name[springboot3-demo,tb_canal_test] , eventType : INSERT
id : 3e71a81fd80711eaaed600163e046cc3    update=true
commodity_name : 叉烧包    update=true
commodity_price : 3.99    update=true
number : 3    update=true
description : 又大又香的叉烧包，老人小孩都喜欢    update=true

```
## 3、Springboot3+ 整合canal 实现实时数据同步到redis