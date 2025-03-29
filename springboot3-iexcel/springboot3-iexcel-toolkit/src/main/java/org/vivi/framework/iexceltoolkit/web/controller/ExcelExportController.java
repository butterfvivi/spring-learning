package org.vivi.framework.iexceltoolkit.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.iexceltoolkit.common.response.R;
import org.vivi.framework.iexceltoolkit.mybatis.entity.ExportBeanConfig;
import org.vivi.framework.iexceltoolkit.mybatis.interfaces.IDAOAdapter;
import org.vivi.framework.iexceltoolkit.mybatis.service.IExport;
import org.vivi.framework.iexceltoolkit.toolkit.core.ExcelInvoke;
import org.vivi.framework.iexceltoolkit.web.request.IDynamicExportReq;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelExportController {

    @Autowired
    private IExport export;

    private IDAOAdapter daoAdapter;

    /**
     *
     * @param response
     * @param dto  {
     *      *                 "**dataList": [导出的数据]
     *      *                 "**headList": [表头，注意下表必须与dataList一致，如果不一致可采用列名与字段的映射，比如第一列名称叫  部门，那么可以直接绑定部门对应的deptName字段。写法：部门@deptName ],
     *      *                 "config":{
     *      *                 "watermark":"水印",
     *      *                 "mergeColIndex":[0,1]--指的是合并的列，比如合并第一列和第二列,
     *      *                 "targetParam":"className@dynamic"---如果导出前，想处理下数据，请求重写的类上加上@MsAsync("className"),方法上加上@MsAsync("dynamic")。
     *              *                                       方法参数接受m(List<Map<String,Object>> data,List<String> headers)
     *      *                 }
     *      *
     * @throws Exception
     */
    @PostMapping("/dynamic")
    public void dynamicExport(HttpServletResponse response, @RequestBody IDynamicExportReq dto) throws Exception {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        dto.setDataList(list);

        ExcelInvoke.dynamicExport(response, dto);
    }

    @PostMapping("/export1")
    public R dynamicExport1(HttpServletResponse response, @RequestBody IDynamicExportReq dto) throws Exception {
        ExportBeanConfig exportBeanConfigDto = new ExportBeanConfig();
        String countSql = "select count(*) from account where id  > 50000";
        String querySql = "SELECT * FROM user \n" +
                "<where>\n" +
                "<if test=\"gender !=null and gender !=''\">\n" +
                "  gender = #{gender}\n" +
                "  </if>\n" +
                "</where>";
        return R.success();
    }
}
