# Getting Started

## docker安装

### 1、安装yum
yum install -y yum-utils \
device-mapper-persistent-data \
lvm2 --skip-broken

### 2、更新本地镜像源，设置docker镜像源
yum-config-manager \
--add-repo \
https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

sed -i 's/download.docker.com/mirrors.aliyun.com\/docker-ce/g' /etc/yum.repos.d/docker-ce.repo

yum makecache fast

### 3、安装docker命令
yum install -y docker-ce


### 4、查看docker版本
docker --version

### 5、启动docker
systemctl start docker
systemctl stop firewalld
systemctl disable firewalld

### 6、配置镜像
cd /etc/docker
sudo vi daemon.json


## docker-compose
### 1、从官网下载docker-compose
https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-linux-x86_64

### 2、赋可执行权
chmod +x /usr/bin/docker-compose

### 3、验证安装
docker-compose -v

### 4、执行命令：
cd /docker
docker-compose up -d elasticsearch

