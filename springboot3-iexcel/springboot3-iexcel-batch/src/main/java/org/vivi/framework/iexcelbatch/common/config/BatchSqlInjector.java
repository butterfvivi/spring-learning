package org.vivi.framework.iexcelbatch.common.config;


import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BatchSqlInjector extends DefaultSqlInjector {


    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        //MyBatis-Plus默认方法
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        //注入InsertBatchSomeColumn。官方标注此处只通过MySQL测试，其他数据库未测试
        // https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/injector/methods/InsertBatchSomeColumn.java
        //在当前当前项目中，pg库测试可用
        //例1: t -> !t.isLogicDelete() , 表示不要逻辑删除字段
        //例2: t -> !t.getProperty().equals("version") , 表示不要字段名为 version 的字段
        //例3: t -> t.getFieldFill() != FieldFill.UPDATE) , 表示不要填充策略为 UPDATE 的字段
        methodList.add(new InsertBatchSomeColumn(t -> !t.getProperty().equals("version")));
        return methodList;
    }
}
