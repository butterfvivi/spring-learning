# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.2/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.2/gradle-plugin/packaging-oci-image.html)
* [MyBatis Framework](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

### Guides

The following guides illustrate how to use some features concretely:

* [MyBatis Quick Start](https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### About Gradle

#### 项目的根目录结构:
##### |-- build # 封装编译后的字节码、打的包、测试报告等信息
##### |-- gradle # 封装包装器文件夹
###### |    |-- wrapper
###### |         |-- gradle-wrapper.jar
###### |         |-- gradle-wrapper.properties
##### |-- gradlew
##### |-- gradlew.bat # 包装器启动脚本
##### |-- build.gradle # 构建脚本，类似于pom.xml
##### |-- settings.gradle # 设置文件，定义项目及子项目名称信息
