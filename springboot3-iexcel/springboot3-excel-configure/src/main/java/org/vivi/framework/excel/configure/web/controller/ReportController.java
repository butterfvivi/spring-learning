package org.vivi.framework.excel.configure.web.controller;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.excel.configure.common.response.R;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.core.service.CommonExportService;
import org.vivi.framework.excel.configure.core.service.IExport;
import org.vivi.framework.excel.configure.core.service.IMapBatchConverter;
import org.vivi.framework.excel.configure.core.service.common.WriteExcelService;
import org.vivi.framework.excel.configure.core.service.converter.DemoMapBatchConverter;
import org.vivi.framework.excel.configure.mybatis.mapper.ISqlMapper;
import org.vivi.framework.excel.configure.web.dto.AccountEntity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/confgiure")
public class ReportController {

    @Autowired
    private IExport export;


    @Autowired
    private CommonExportService commonExportService;

    @Autowired
    private WriteExcelService writeExcelService;

    @Resource(name = "defaultMapBatchConverter")
    private IMapBatchConverter defaultMapBatchConverter;

    @PostMapping("/export1")
    public void testExport1(HttpServletResponse response) {

        long startTime = System.currentTimeMillis();
        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";

        // String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account $LIMIT_CYCLE_COUNT";
        //String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
        //        " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) and id > 50000 LIMIT $LIMIT_COUNT";

        String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
                " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) LIMIT $LIMIT_COUNT";
        exportBeanConfigDto.setCountSql(countSql);
        exportBeanConfigDto.setQuerySql(querySql);
        exportBeanConfigDto.setLimitCount(30000);
        exportBeanConfigDto.setSize(30000);
        exportBeanConfigDto.setUseObjectModel(false);
        exportBeanConfigDto.setUseParallelQuery(true);
        exportBeanConfigDto.setT(new Object());

        List list = new ArrayList();
        try {

            list = export.getExportList(exportBeanConfigDto);

            List<List<String>> headData = new ArrayList<>();
            headData.add(List.of("id", "accountNumber", "password", "clientId")); // 默认表头

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment; filename*=utf-8''" + "test2");
            OutputStream os = response.getOutputStream();
            EasyExcel.write(os)
                    .registerConverter(new DemoMapBatchConverter())
                    // 这里放入动态头
                    .head(list)
                    .sheet("模板")
                    // 当然这里数据也可以用 List<List<String>> 去传入
                    .doWrite(list);
            long endTime = System.currentTimeMillis();
            System.out.println("endTime - startTime = "+(endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/export2")
    public void testExport2(HttpServletResponse response) {

        long startTime = System.currentTimeMillis();
        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";

        // String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account $LIMIT_CYCLE_COUNT";
        //String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
        //        " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) and id > 50000 LIMIT $LIMIT_COUNT";

        String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
                " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) LIMIT $LIMIT_COUNT";
        exportBeanConfigDto.setCountSql(countSql);
        exportBeanConfigDto.setQuerySql(querySql);
        exportBeanConfigDto.setLimitCount(30000);
        exportBeanConfigDto.setSize(30000);
        exportBeanConfigDto.setUseObjectModel(false);
        exportBeanConfigDto.setUseParallelQuery(true);
        exportBeanConfigDto.setT(new AccountEntity());

        List list = new ArrayList();
        try {

            list = export.getExportList(exportBeanConfigDto);

            writeExcelService.writeListMap2Excel(list,
                    "E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\springboot3-excel-configure\\src\\main\\resources\\test1.xlsx",
                    exportBeanConfigDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/export3")
    public void testExport3(HttpServletResponse response) {

        long startTime = System.currentTimeMillis();
        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";

        // String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account $LIMIT_CYCLE_COUNT";
        //String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
        //        " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) and id > 50000 LIMIT $LIMIT_COUNT";

        String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
                " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) LIMIT $LIMIT_COUNT";
        exportBeanConfigDto.setCountSql(countSql);
        exportBeanConfigDto.setQuerySql(querySql);
        exportBeanConfigDto.setLimitCount(30000);
        exportBeanConfigDto.setSize(30000);
        exportBeanConfigDto.setUseObjectModel(false);
        exportBeanConfigDto.setUseParallelQuery(true);
        exportBeanConfigDto.setT(new HashMap<>());

        List list = new ArrayList();
        try {

            list = export.getExportList(exportBeanConfigDto);

            writeExcelService.writeListMap2Excel(list,
                    "E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\springboot3-excel-configure\\src\\main\\resources\\test1.xlsx",
                    exportBeanConfigDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
