package org.vivi.framework.iexceltoolkit.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.utils.AssertUtil;
import org.vivi.framework.iexceltoolkit.web.request.ImportReq;
import org.vivi.framework.iexceltoolkit.toolkit.core.ExcelInvoke;
import org.vivi.framework.iexceltoolkit.web.request.IDynamicExportReq;
import org.vivi.framework.iexceltoolkit.web.request.ITemplateExportReq;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelImportController {


    @PostMapping("/import")
    public Object readExcel(@RequestParam("file") MultipartFile file,
                            @RequestParam("targetParam") String targetParam,
                            @RequestParam("headRow") Integer headRow,
                            @RequestParam(value = "remark", defaultValue = "") String remark) {
        ImportReq dto = new ImportReq(targetParam, headRow, remark);
        Object result = null;
        try {
            result = ExcelInvoke.importExcel(file, dto);
        } catch (Exception e) {
            AssertUtil.throwInnerException(e.getMessage());
        }
        return result;
    }

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
    @PostMapping("/dynamic/export")
    public void dynamicExport(HttpServletResponse response, @RequestBody IDynamicExportReq dto) throws Exception {
        ExcelInvoke.dynamicExport(response, dto);
    }


    /**
     *
     * @param response
     * @param dto {
     *      *                 "**templatePath": "导出模板名称。模板必须全部方法resources目录下"
     *      *                 "**dataList":[导出的是数据],
     *      *                 "otherVal": {一些额外的数据，比如合计、总数等等,也可以是查询条件等},
     *      *                 "config":{
     *      *                 "watermark":"水印",
     *      *                 "mergeColIndex":[0,1]--指的是合并的列，比如合并第一列和第二列,
     *      *                 “excludeRowIndex”:[0,1]--指的是模板导出的表头所占的行数，不参与和并列逻辑
     *      *                 "targetParam":"className@template"---如果导出前，想处理下数据，请求重写的类上加上@MsExcelRewrite("className"),
     *                      *                                       方法上加上@MsExcelRewrite("template")。方法参数接受m(List<Map<String,Object>> data, Map<String,Object> otherVal)
     *      *                 }
     *      * }
     * @throws Exception
     */
    @PostMapping("/template/export")
    public void templateExport(HttpServletResponse response, @RequestBody ITemplateExportReq dto) throws Exception {
        ExcelInvoke.templateExport(response, dto);
    }

}
