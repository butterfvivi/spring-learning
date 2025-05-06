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



10w条数据导出到excel对比
```  
使用EasyExcel,单表导出excel 10.73s  50.86M
导出Excel(先查后写)         11.39s  50.86M  
导出Excel（并发查询，串行写入）10.36 s 50.86 M
导出Excel[下载速度可能更快]（并发查询，串行写入） 10.33s 50.86M
导出Excel（流式查询） 11.58s 50.86M
```