Spring Authorization Server 授权码模式测试流程
    1、依次启动 oauth2-security-server、oauth2-security-client,oauth2-security-gateway
    2、浏览器输入网关： http://127.0.0.1:2002/client
    3、因为第一次未授权会跳转到认证中心进行认证，输入用户名/密码(test/test)，认证成功网关会路由到资源服务器获取数据。