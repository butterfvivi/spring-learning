# Getting Started

# Springboot3集成elasticsearch8.x的三种实现方式

## 方式1：使用ElasticsearchRepository
ElasticsearchRepository 是Spring Data Elasticsearch项目中的一个接口，用于简化对Elasticsearch集
群的CRUD操作以及其他高级搜索功能的集成。这个接口允许开发者通过声明式编程模型来执行数据
持久化操作，从而避免直接编写复杂的REST API调用代码。

## 方式2：使用ElasticsearchTemplate
ElasticsearchTemplate模板类，封装了便捷操作Elasticsearch的模板方法，包括 索引 / 映射 / 文档
CRUD 等底层操作和高级操作

## 方式3：使用ElasticsearchClient
从 Java Rest Client 7.15.0 版本开始，Elasticsearch 官方决定将 RestHighLevelClient 标记为废弃的，
并推荐使用新的 Java API Client，即 ElasticsearchClient


# Elasticsearch 处理关联关系
Elasticsearch多表关联的问题是讨论最多的问题之一。多表关联通常指一对多或者多对多的数据关
系，如博客及其评论的关系。
Elasticsearch并不擅长处理关联关系，一般会采用以下四种方法处理关联：
* Nested类型适用于一对少量、子文档偶尔更新、查询频繁的场景。如果需要索引对象数组并保持
数组中每个对象的独立性，则应使用Nested数据类型而不是Object数据类型。
Nested类型的优点是Nested文档可以将父子关系的两部分数据关联起来（例如博客与评论），可以基
于Nested类型做任何查询。其缺点则是查询相对较慢，更新子文档时需要更新整篇文档。
* Join类型用于在同一索引的文档中创建父子关系。Join类型适用于子文档数据量明显多于父文档的
数据量的场景，该场景存在一对多量的关系，子文档更新频繁。举例来说，一个产品和供应商之间就
是一对多的关联关系。当使用父子文档时，使用has_child或者has_parent做父子关联查询。
Join类型的优点是父子文档可独立更新。缺点则是维护Join关系需要占据部分内存，查询较Nested类型
更耗资源。
* 宽表冗余存储
宽表适用于一对多或者多对多的关联关系。
   
宽表的优点是速度快。缺点则是索引更新或删除数据时，应用程序不得不处理宽表的冗余数据；并
且由于冗余存储，某些搜索和聚合操作的结果可能不准确。
 * 业务端关联
这是普遍使用的技术，即在应用接口层面处理关联关系。一般建议在存储层面使用两个独立索引存
 储，在实际业务层面这将分为两次请求来完成。
 业务端关联适用于数据量少的多表关联业务场景。数据量少时，用户体验好；而数据量多时，两次
查询耗时肯定会比较长，反而影响用户体验

## 多表关联方案对比
在Elasticsearch开发实战中，对于多表关联的设计要突破关系型数据库设计的思维定式。不建议在Elasticsearch中做多表关联操作，尽量在设计时使用扁平的宽表文档模型，或者尽量将业务转化为没有关联关系的文档形式，在文档建模处多下功夫，以提升检索效率。
