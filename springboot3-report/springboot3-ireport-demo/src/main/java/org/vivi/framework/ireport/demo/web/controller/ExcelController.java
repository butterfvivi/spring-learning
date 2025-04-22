package org.vivi.framework.ireport.demo.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.ireport.demo.report.IExcelInvoke;
import org.vivi.framework.ireport.demo.common.utils.AssertUtils;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;
import org.vivi.framework.ireport.demo.web.request.ITemplateExportDto;
import org.vivi.framework.ireport.demo.web.request.ImportExcelDto;

@RestController
@RequestMapping("api/iexcel")
public class ExcelController {

    @Autowired
    private IExcelInvoke iExcelInvoke;

    /**z
     * @param response
     * @param dto      {
     *                 "**dataList": [导出的数据]
     *                 "**headList": [表头，注意下表必须与dataList一致，如果不一致可采用列名与字段的映射，比如第一列名称叫  部门，那么可以直接绑定部门对应的deptName字段。写法：部门@deptName ],
     *                 "config":{
     *                 "mergeColIndex":[0,1]--指的是合并的列，比如合并第一列和第二列,
     *                 "targetParam":"className@dynamic"---如果导出前，想处理下数据，请求重写的类上加上@MsAsync("className"),方法上加上@MsAsync("dynamic")。方法参数接受m(List<Map<String,Object>> data,List<String> headers)
     *                 }
     *                 }
     * @return
     */

    @PostMapping("/dynamicExport")
    public void dynamicExport(HttpServletResponse response, @RequestBody IDynamicExportDto dto) throws Exception {
        iExcelInvoke.dynamicExport(response, dto);
    }

    /**
     * @param response
     * @param dto      {
     *                 "**templatePath": "导出模板名称。模板必须全部方法resources目录下"
     *                 "**dataList":[导出的是数据],
     *                 "otherVal": {一些额外的数据，比如合计、总数等等,也可以是查询条件等},
     *                 "config":{
     *                 "mergeColIndex":[0,1]--指的是合并的列，比如合并第一列和第二列,
     *                 “excludeRowIndex”:[0,1]--指的是模板导出的表头所占的行数，不参与和并列逻辑
     *                 "targetParam":"className@template"---如果导出前，想处理下数据，请求重写的类上加上@MsExcelRewrite("className"),方法上加上@MsExcelRewrite("template")。方法参数接受m(List<Map<String,Object>> data, Map<String,Object> otherVal)
     *                 }
     *                 }
     * @return
     */
    @PostMapping("/templateExport")
    public void templateExport(HttpServletResponse response, @RequestBody ITemplateExportDto dto) throws Exception {
        iExcelInvoke.templateExport(response, dto);
    }

    /**
     * 全局通用的excel解析
     *
     * @param file        文件对象
     * @param targetParam {"targetParam":"目标参数，用于查询导出需要映射的实体",}
     *                    注意：导入targetParam必须有值，并且方法上需要指定映射的那个实体类。
     * @param headRow     指定从第一行开始读
     * @param remark      导入一些备注，可以传入任意值
     * @return 解析后的数据集合
     */

    @PostMapping("/import")
    public Object readExcel(@RequestParam("file") MultipartFile file,
                            @RequestParam("targetParam") String targetParam,
                            @RequestParam("headRow") Integer headRow,
                            @RequestParam(value = "remark", defaultValue = "") String remark) {
        ImportExcelDto dto = new ImportExcelDto(targetParam, headRow,remark);
        Object result = null;
        try {
            result = iExcelInvoke.importExcel(file, dto);
        } catch (Exception e) {
            AssertUtils.throwInnerException(e.getMessage());
        }
        return result;
    }
}
