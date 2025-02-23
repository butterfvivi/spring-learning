package org.vivi.framework.excel.configure.web.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.excel.configure.common.response.R;
import org.vivi.framework.excel.configure.core.entity.ExportBeanConfig;
import org.vivi.framework.excel.configure.core.service.CommonExportService;
import org.vivi.framework.excel.configure.core.service.IExport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportDataController {

    @Autowired
    private IExport export;


    @Autowired
    private CommonExportService commonExportService;

    @GetMapping("/data")
    public R getReportData(){
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
            System.out.println(list.size() + "================");
            long endTime = System.currentTimeMillis();
            System.out.println("endTime - startTime = "+(endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.success(list);
    }

    @PostMapping("/batch")
    public void insert()   {

        List<String> sqlList = new ArrayList<>(1000000);

        for (int i = 500000; i < 700000; i++) {
            StringBuilder builder = new StringBuilder("insert  into `account`(`id`,`account_number`,`password`,`money`,`client_id`) ");
            String sql = "values (" + i + ",'" + i + "iui3u23inskadnfkasdf" + i + "','" + i + "','" + i + "'," + i + ");";
            builder.append(sql);
            sqlList.add(builder.toString());
            System.out.println(builder);
        }
        File file = new File("E:\\IdeaProjects\\springboot3-learning\\springboot3-iexcel\\springboot3-excel-configure\\src\\main\\resources\\batchSql.sql");

        try {
            FileUtils.writeLines(file, sqlList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/export")
    public List testExport()   {
        long startTime = System.currentTimeMillis();

        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";

        // String querySql = "SELECT id,account_number AS accountNumber,PASSWORD,client_id AS clientId FROM account";
        String querySql = "SELECT id,account_number AS accountNumber,password,client_id as clientId FROM account " +
                " where id >= (SELECT id FROM account   LIMIT $LIMIT_CYCLE_COUNT, 1 ) and id > 50000 LIMIT $LIMIT_COUNT";
        exportBeanConfigDto.setCountSql(countSql);
        exportBeanConfigDto.setQuerySql(querySql);
        exportBeanConfigDto.setLimitCount(30000);
        exportBeanConfigDto.setSize(30000);
        exportBeanConfigDto.setUseObjectModel(false);
        exportBeanConfigDto.setUseParallelQuery(true);
        //exportBeanConfigDto.setT(new AccountEntity());
        //exportBeanConfigDto.setT(Lists.newArrayList("id", "accountNumber", "password", "clientId"));

        List list = new ArrayList();
        try {

            list = commonExportService.getExportData(exportBeanConfigDto);
            System.out.println(list.size() + "================");
            long endTime = System.currentTimeMillis();
            System.out.println("endTime - startTime = "+(endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
