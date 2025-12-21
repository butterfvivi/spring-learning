

```shell
docker-compose -f docker-compose-canal.yml up -d


3306 数据库, 创建cancal的账号密码

CREATE USER 'canal'@'%' IDENTIFIED BY 'canal'; --创建账号密码
 
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%'; --账号密码授权
 
select host,user,plugin from mysql.user ;
 
ALTER USER 'canal'@'%' IDENTIFIED WITH mysql_native_password BY 'canal'; --user表plugin 需要为mysql_native_password类型
 
 
GRANT ALL PRIVILEGES ON dtm_busi.* TO 'canal'@'%'; --给canal用户授权
 
 
FLUSH PRIVILEGES; --刷新权限 重启数据库

```
