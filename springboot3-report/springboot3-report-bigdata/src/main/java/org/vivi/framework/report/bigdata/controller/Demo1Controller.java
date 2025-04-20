package org.vivi.framework.report.bigdata.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.report.bigdata.entity.Cinfo;
import org.vivi.framework.report.bigdata.paging1.export.ExportUtil;
import org.vivi.framework.report.bigdata.paging1.funtion.LambdaExportFunction;
import org.vivi.framework.report.bigdata.paging1.listener.EasyExcelGeneralDataListener;
import org.vivi.framework.report.bigdata.service.DemoService1;

import java.util.List;
import java.util.function.Consumer;


@Slf4j
@RestController
@RequestMapping("/demo1")
public class Demo1Controller {

    @Resource
    private DemoService1 demoService;

    @GetMapping("/export1")
    public void export1(HttpServletResponse response)   {
        LambdaQueryWrapper<Cinfo> empLambdaQueryWrapper = new LambdaQueryWrapper<>();

        ExportUtil<Cinfo> empExportUtil = new ExportUtil<Cinfo>(Cinfo.class);

        empExportUtil.exportExcel(new LambdaExportFunction(demoService, empLambdaQueryWrapper));
    }


    @GetMapping("/export2")
    public void export2(HttpServletResponse response)   {
        LambdaQueryWrapper<Cinfo> empLambdaQueryWrapper = new LambdaQueryWrapper<>();

        ExportUtil<Cinfo> empExportUtil = new ExportUtil<Cinfo>(Cinfo.class);

        empExportUtil.exportExcel(response,new LambdaExportFunction(demoService, empLambdaQueryWrapper), "demo1", "demo1");
    }

    @GetMapping("/importExcel")
    public void importExcel(HttpServletResponse response)   {
        Consumer<List<Cinfo>> objectConsumer = new Consumer<List<Cinfo>>() {


            @Override
            public void accept(List<Cinfo> emps) {
//
                System.out.println(emps.size() + "demo条数");
                System.out.println("sss");
            }
        };

        //读取所有Sheet的数据.每次读完一个Sheet就会调用这个方法
        EasyExcel.read("/Users/vivi/IdeaProjects/spring-learning/springboot3-report/springboot3-report-bigdata/src/main/resources/excel/dd.xlsx", new EasyExcelGeneralDataListener<Cinfo>(3, objectConsumer))
                .extraRead(CellExtraTypeEnum.COMMENT)
                .extraRead(CellExtraTypeEnum.MERGE)
                .doReadAll();
    }
}
