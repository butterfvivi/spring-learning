# Getting Started

#### excel文件导出 (支持xls和xlsx)
```
1. excel文件导出
    支持自定义title导出
        2种不同的方式,设置excel表头
    支持指定模板文件导出
        指定excel模板文件流
        
    自定义utils -> poi(package)
excel文件导入
    支持自定义title导入
        2种不同的方式,设置excel表头
    支持指定列,指定行导入(无title)
 
自定义utils -> poi(package)
test -> ExcelsTest/ Interface2

``` 


#### 读写分离分页导出大量Excel数据
```   
2. Java读写分离导出Excel数据 使用SpringBoot3+JDK21+EasyExcel,单表导出excel在32s左右

分批处理util  -> paging
处理的service  -> DemoService1
test -> MyBatisFunction1Test
```


#### 分批查询的数据分批写入到Excel
```   
3.分批查询的数据分批写入到Excel中

分批处理util  -> paging1
处理的service  -> DemoService
test -> ParallelUtilTest / SlidingWindowTest
```
