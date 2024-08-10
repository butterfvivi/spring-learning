package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.entity.dto.DataExcelImportDto;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.listener.AnalysisListener;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;
import org.vivi.framework.iexcelbatch.processor.AsyncBatchProcessor;
import org.vivi.framework.iexcelbatch.service.ExcelBatchService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
public class ExcelBatchServiceImpl implements ExcelBatchService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AsyncBatchProcessor asyncBatchProcessor;


    @Async
    @Override
    public CompletableFuture<Integer> asyncImport1(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader excelReader = EasyExcel.read(inputStream, User.class, new AnalysisListener()).build();
        excelReader.readAll();
        excelReader.finish();
        return CompletableFuture.completedFuture(1);
    }


    @Override
    @Transactional(rollbackFor = Exception.class) // 添加事务，异常则回滚所有导入
    public DataExcelImportDto asyncImport2(UserRequest userRequest, MultipartFile file) {
        DataExcelImportDto respVO = null;
        TimeInterval timer = DateUtil.timer();
        try {
            //开始计时
            respVO = asyncBatchProcessor.readExcelAndSaveAsync(User.class,
                    file,
                    data -> {
                        User user = new User();
                        //数据预处理，因为目前的批处理方法不会再自动填充数据，所以这里手动填充
                        user.setCreateTime(LocalDateTime.now());
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
        }catch (Exception e){
            e.printStackTrace();
        }
        //结束计时
        long interval = timer.interval();
        log.info("导入数据共花费：{}s", interval / 1000);
        return respVO;
    }

    @Override
    public DataExcelImportDto asyncImport3(UserRequest userRequest, MultipartFile file) throws IOException {
        TimeInterval timer = DateUtil.timer();
        DataExcelImportDto respVO = null;
        asyncBatchProcessor.readExcelAndSaveAsync2(User.class
                , file
                ,  data -> {
                    User user = new User();
                    user.setCreateTime(LocalDateTime.now());
                    return user;
                }
                , userMapper::insertBatchSomeColumn
        );
        //结束计时
        long interval = timer.interval();
        log.info("导入数据共花费：{}s", interval / 1000);
        respVO.setTime(interval);
        return respVO;
    }

    @Override
    public void asyncbatchImport(UserRequest userRequest, MultipartFile[] files) {
        TimeInterval timer = DateUtil.timer();
        asyncBatchProcessor.readExcelAndSaveAsync3(User.class
                , files
                , data -> {
                    User user = new User();
                    user.setCreateTime(LocalDateTime.now());
                    return user;
                }
                , userMapper::insertBatchSomeColumn
        );
        //结束计时
        long interval = timer.interval();
        log.info("导入数据共花费：{}s", interval / 1000);
    }
}
