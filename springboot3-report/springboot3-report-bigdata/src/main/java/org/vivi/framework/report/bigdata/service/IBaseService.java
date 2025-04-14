package org.vivi.framework.report.bigdata.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * 基础业务层接口
 *
 * @author chenxueyong
 * @date 2023/03/15
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 更新批量查询包装
     *
     * @param entityList           实体列表
     * @param queryWrapperFunction 查询包装器函数
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateBatchByQueryWrapper(Collection<T> entityList, Function<T, Wrapper<T>> queryWrapperFunction);

    /**
     * 分页查询并消费全部数据
     * @param pageSize 页大小
     * @param queryWrapper 查询条件
     * @param pageConsumer 消费者
     */
    void page(long pageSize, Wrapper<T> queryWrapper, Consumer<IPage<T>> pageConsumer);

    /**
     * 页面获取
     * 流式分页查询并消费全部数据<br>
     * 注意：使用原生MyBatis查询，MyBatis-Plus相关特性不支持(如逻辑删除字段等)
     * @param queryWrapper 查询包装
     * @param pageConsumer 页面消费者
     * @return long
     */
    long pageForFetch(Wrapper<T> queryWrapper, Consumer<IPage<T>> pageConsumer);


    /**
     * 写excel
     *
     * @param outputStream 输出流
     * @param pageSize 页大小
     * @param queryWrapper 查询条件
     */
    void writeExcel(OutputStream outputStream, long pageSize, Wrapper<T> queryWrapper);

    /**
     * 写入Excel（并发查询数据，有序串行写入数据）
     *
     * @param outputStream 输出流
     * @param parallelNum  并发线程数
     * @param pageSize     页大小
     * @param queryWrapper 查询条件
     * @throws InterruptedException 中断异常
     */
    void writeExcelForParallel(OutputStream outputStream, int parallelNum, long pageSize, Wrapper<T> queryWrapper) throws InterruptedException;

    /**
     * 写入Excel[写入速度可能更快]（并发查询数据，有序串行写入数据）
     * @param outputStream 输出流
     * @param parallelNum 并发线程数
     * @param pageSize 页大小
     * @param queryWrapper 查询条件
     */
    void writeExcelForXParallel(OutputStream outputStream, int parallelNum, int pageSize, Wrapper<T> queryWrapper) throws InterruptedException, ExecutionException;


    /**
     * 写excel导入模板
     *
     * @param outputStream 输出流
     */
    void writeExcelImportTemplate(OutputStream outputStream);

    /**
     * 写入Excel（流式查询数据并写入）<br>
     * 注意：使用原生MyBatis查询，MyBatis-Plus相关特性不支持(如逻辑删除字段、下划线自动转驼峰等)
     * @param outputStream 输出流
     * @param queryWrapper 查询条件
     */
    void writeExcelForFetch(OutputStream outputStream, Wrapper<T> queryWrapper);

}
