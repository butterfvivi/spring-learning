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
import org.vivi.framework.report.bigdata.service.CinfoService;
import org.vivi.framework.report.bigdata.service.DemoService;
import org.vivi.framework.report.bigdata.service.DemoService1;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.function.Consumer;


@Slf4j
@RestController
@RequestMapping("/demo1")
public class Demo1Controller {

    @Resource
    private DemoService1 demoService1;

    @Resource
    private DemoService demoService;

    @Resource
    private CinfoService cinfoService;

    @GetMapping("/export1")
    public void export1(HttpServletResponse response)   {
        LambdaQueryWrapper<Cinfo> empLambdaQueryWrapper = new LambdaQueryWrapper<>();

        ExportUtil<Cinfo> empExportUtil = new ExportUtil<Cinfo>(Cinfo.class);

        empExportUtil.exportExcel(new LambdaExportFunction(demoService1, empLambdaQueryWrapper));
    }


    @GetMapping("/export2")
    public void export2(HttpServletResponse response)   {
        LambdaQueryWrapper<Cinfo> empLambdaQueryWrapper = new LambdaQueryWrapper<>();

        ExportUtil<Cinfo> empExportUtil = new ExportUtil<Cinfo>(Cinfo.class);

        empExportUtil.exportExcel(response,new LambdaExportFunction(demoService1, empLambdaQueryWrapper), "demo1", "demo1");
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

    /**
     * easyexcel导出excel
     * @param response
     */
    @GetMapping("/export3")
    public void export3(HttpServletResponse response) throws IOException {
        try {
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 准备数据
            List<Cinfo> list = cinfoService.list();

            // 写入Excel
            EasyExcel.write(response.getOutputStream(), Cinfo.class)
                    .sheet("用户列表")
                    .doWrite(list);

        } catch (IOException e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("下载文件失败" + e.getMessage());
        }
    }
}
