package org.vivi.framework.report.bigdata;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.report.bigdata.entity.Demo;
import org.vivi.framework.report.bigdata.service.DemoService1;
import org.vivi.framework.report.bigdata.paging1.export.ExportUtil;
import org.vivi.framework.report.bigdata.paging1.funtion.LambdaExportFunction;
import org.vivi.framework.report.bigdata.paging1.listener.EasyExcelGeneralDataListener;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;


@SpringBootTest
class MyBatisFunction1Test {


    @Resource
    private DemoService1 demoService;

    @Test
    void export() throws IOException {

        LambdaQueryWrapper<Demo> empLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        Function<Page, List<Emp>> exportFunc = new SingleTableExportFuc<>(empService, empLambdaQueryWrapper);

        ExportUtil<Demo> empExportUtil = new ExportUtil<Demo>(Demo.class);

        empExportUtil.exportExcel(new LambdaExportFunction(demoService, empLambdaQueryWrapper));
    }

    @Test
    void importData() throws IOException {
        Consumer<List<Demo>> objectConsumer = new Consumer<List<Demo>>() {


            @Override
            public void accept(List<Demo> emps) {
//
                System.out.println(emps.size() + "demo条数");
                System.out.println("sss");
            }
        };

        //读取所有Sheet的数据.每次读完一个Sheet就会调用这个方法
        EasyExcel.read("G:/dd.xlsx", new EasyExcelGeneralDataListener<Demo>(3, objectConsumer))
                .extraRead(CellExtraTypeEnum.COMMENT)
                .extraRead(CellExtraTypeEnum.MERGE)
                .doReadAll();
    }
}
