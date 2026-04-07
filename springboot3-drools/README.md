# Springboot3 集成 Drools 8.+

## Springboot3-drools-simple 
模块是简单的 Spring Boot 3 集成 Drools 8.+ 的示例项目，包含以下功能：
- Drools 规则引擎的基本使用
- 规则文件的加载和执行
- DroolsController 类，网上购物，需要根据不同的规则计算商品折扣，比如VIP客户增加5%的折扣，购买金额超过1000元的增加10%的折扣等，而且这些规则可能随时发生变化，甚至增加新的规则

## Springboot3-drools-demo 
实现将drools规则文件加载到内存中，并可以动态执行规则，用于演示如何使用 Drools 8.+ 规则引擎进行规则管理。
- KieFileSystem 类，用于加载规则文件
- KieContainer 类，用于创建 KieSession
- KieSession 类，用于执行规则
- KieBase 类，用于管理规则库
- KieServices 类，用于获取 KieServices 实例
- KieModule 类，用于定义规则库的版本和名称，创建 KieModule

将规则存储在数据库中，增删改差规则，然后按照名字来执行规则

## Springboot3-mall-rules 
模块是一个电商平台的规则管理模块，用于管理电商平台的规则，包括商品折扣规则、订单优惠规则等，这些规则可以动态加载和执行，用于实现电商平台的规则管理功能。
- RewardRuleController 类，用于管理奖励规则