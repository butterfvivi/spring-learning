# springboot3-activiti 报表设计器

#### 介绍
springboot3 集成activiti8
activiti8以后只支持java17以上版本，而activiti 8.6 只支持java 21以上版本


#### 第一步 添加仓库
```xml
<repositories>
    <repository>
        <id>activiti-releases</id>
        <url>https://artifacts.alfresco.com/nexus/content/repositories/activiti-releases</url>
    </repository>
</repositories>
```

activiti7以后官方不再将构建制品发布到maven中央仓库了，而是发布在了alfresco仓库，这就需要我们仓库才能正确下载依赖.在pom中添加repositories标签，然后再添加repository标签



#### 第二步 添加依赖
```xml
   <!--activity 依赖-->
        <dependency>
            <groupId>org.activiti</groupId>
            <artifactId>activiti-spring-boot-starter</artifactId>
            <version>8.6.0</version>
        </dependency>
```


