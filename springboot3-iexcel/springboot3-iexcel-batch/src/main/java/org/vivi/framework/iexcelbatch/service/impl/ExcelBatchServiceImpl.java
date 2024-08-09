package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.listener.AnalysisListener;
import org.vivi.framework.iexcelbatch.listener.CustomReadListener;
import org.vivi.framework.iexcelbatch.listener.PageReadListener;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;
import org.vivi.framework.iexcelbatch.processor.BatchDataProcessor;
import org.vivi.framework.iexcelbatch.service.ExcelBatchService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExcelBatchServiceImpl implements ExcelBatchService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BatchDataProcessor batchDataProcessor;

    @Override
    public void asyncImport2(Class<T> head,  MultipartFile file,  Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(10);


        List<String> errorNames = Lists.newCopyOnWriteArrayList();
        //String userName = SecurityAuthorHolder.getSecurityUser().getUsername();
        Collection<CompletableFuture<int[]>> allFutures = new ArrayList<>();
        EasyExcel.read(file.getInputStream(),head,new PageReadListener<T>(dataList -> {
            CompletableFuture.allOf(
                    CompletableFuture.runAsync(
                            () -> {
                                List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
                                allFutures.add(batchDataProcessor.saveAsyncBatch2(list,dbFunction));
                            }, executor)
            ).join();
        })).sheet().doRead();

        // 等待所有 CompletableFuture 完成
        //allFutures.join();
        // 关闭线程池
        executor.shutdown();
    }


    public void batchUpload2(Class<T> head, MultipartFile[] files, Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(10);


        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                Arrays.stream(files).map(v ->
                        CompletableFuture.runAsync(
                                () -> {
                                    try {
                                        EasyExcel.read(v.getInputStream(), head, new PageReadListener<T>(dataList -> {
                                            //转换DO，并设置数据源id
                                            List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
                                            //异步批量插入
                                            batchDataProcessor.saveAsyncBatch(list,dbFunction);
                                        })).sheet().doRead();
                                    } catch (IOException e) {
                                    }
                                }, executor)
                ).toArray(CompletableFuture[]::new)
        );

        // 等待所有 CompletableFuture 完成
        allFutures.join();
        // 关闭线程池
        executor.shutdown();
    }

    @Async
    public CompletableFuture<Integer> batchUpload3(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = EasyExcel.read(inputStream, User.class, new AnalysisListener()).build();
        excelReader.readAll();
        excelReader.finish();
        return CompletableFuture.completedFuture(1);
    }


    /**
     * 异步多线程导入数据
     * 采用自定义注入mybatis-plus的SQL注入器，实现真正的BatchInsert，但是需要注意的是项目配置文件需要在jdbc的url后面加上rewriteBatchedStatements=true
     * @param head  Excel导入实体类的class
     * @param file  要导入的Excel文件
     * @param function  数据处理函数，对数据加工
     * @param dbFunction  数据库操作
     * @return 导入结果
     * @param <T>  Excel导入实体类   例如DataSwMbqcdssImportExcelVO
     * @param <R>  数据库实体类  例如DataSwMbqcdssDO
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public  <T,R> DataExcelImportDto readExcelAndSaveAsync(Class<T> head, MultipartFile file, Function<T,R> function, Function<List<R>,Integer> dbFunction) throws IOException, ExecutionException, InterruptedException {
        Integer successCount = 0;
        Integer failCount = 0;
        //存储异步线程的执行结果
        Collection<Future<int[]>> futures = new ArrayList<>();

        EasyExcel.read(file.getInputStream(), head, new PageReadListener<T>(dataList -> {
            //转换DO，并设置数据源id
            List<R> list = dataList.parallelStream().map(function).collect(Collectors.toList());
            //异步批量插入
            futures.add(batchDataProcessor.saveAsyncBatch(list,dbFunction));
        })).sheet().doRead();
        //等待异步线程执行完毕
        for (Future<int[]> future : futures) {
            int[] counts = future.get();
            successCount += counts[0];
            failCount += counts[1];
        }
        log.info("存储成功总数据量：{},存储失败总数据量:{}", successCount,failCount);
        DataExcelImportDto respVO = DataExcelImportDto.builder().successCount(successCount).failCount(failCount).build();
        return respVO;
    }

    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public DataExcelImportDto asyncImport(UserRequest userRequest, MultipartFile file) throws IOException, ExecutionException, InterruptedException {
        //新增数据源，或者id
        //User functionDatasource = User.INSTANCE.convert(createReqVO);
        //userMapper.insert(functionDatasource);
        //Long id = functionDatasource.getId();
        //获取当前登录用户
        //Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        //开始计时
        TimeInterval timer = DateUtil.timer();
        //DataExcelImportRespVO respVO = readExcelAndSave(DataSwMbqcdssImportExcelVO.class, file, data -> DataSwMbqcdssConvert.INSTANCE.convert(data).setDsId(id), dataSwMbqcdssInsertMapper::saveBatch);
        DataExcelImportDto respVO = readExcelAndSaveAsync(User.class,
                file,
                data -> {
                    User user = new User();
                    //数据预处理，因为目前的批处理方法不会再自动填充数据，所以这里手动填充
                    user.setCreateTime(new Date());
                    return user;
                },
                userMapper::insertBatchSomeColumn);
        //结束计时
        long interval = timer.interval();
        log.info("导入数据共花费：{}s", interval / 1000);
        respVO.setTime(interval);
        // 存储文件
        //saveDsFile(id,file);
        return respVO;
    }
}
