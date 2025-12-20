# Getting Started



# 1 Seata 架构
在 Seata 的架构中，一共有三个角色：
- TC (Transaction Coordinator) - 事务协调者
维护全局和分支事务的状态，驱动全局事务提交或回滚。
- TM (Transaction Manager) - 事务管理器
定义全局事务的范围：开始全局事务、提交或回滚全局事务。
- RM (Resource Manager) - 资源管理器
管理分支事务处理的资源，与 TC 交谈以注册分支事务和报告分支事务的状态，并驱动分支
事务提交或回滚。
其中，TC 为单独部署的 Server 服务端，TM 和 RM 为嵌入到应用中的 Client 客户端。


在 Seata 中，一个分布式事务的生命周期如下：
TM 请求 TC 开启一个全局事务。TC 会生成一个 XID 作为该全局事务的编号。XID
会在微服务的调用链路中传播，保证将多个微服务的子事务关联在一起。
RM 请求 TC 将本地事务注册为全局事务的分支事务，通过全局事务的 XID 进行关
联。
TM 请求 TC 告诉 XID 对应的全局事务是进行提交还是回滚。
TC 驱动 RM 们将 XID 对应的自己的本地事务进行提交还是回滚。

# 2 Seata 安装部署

参考 Springboot3-docker 中的 seata 目录
* [Seata 官方文档](https://seata.io/zh-cn/docs/)

