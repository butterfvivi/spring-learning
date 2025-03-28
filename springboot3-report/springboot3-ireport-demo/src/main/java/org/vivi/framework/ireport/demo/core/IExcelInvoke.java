package org.vivi.framework.ireport.demo.core;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.ireport.demo.core.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.utils.IocUtil;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;
import org.vivi.framework.ireport.demo.web.request.ITemplateExportDto;
import org.vivi.framework.ireport.demo.web.request.ImportExcelDto;

public class IExcelInvoke {

    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);

    /**
     * 导入数据解析，要求必须有一个实体类，目的是为了便于后期人员维护，和解析数据方便
     *
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     */

    public static Object importExcel(MultipartFile file, ImportExcelDto dto) throws Exception {
        return ExcelInvokeCore.importExcel(file, dto);
    }

    /**
     * 动态导出
     * @param response
     * @param req
     */
    public static void dynamicExport(HttpServletResponse response, IDynamicExportDto req) throws Exception {
        ExcelInvokeCore.dynamicExport(response, req);
    }

    /**
     * 模板导出
     * @param response
     * @param req
     */
    public static void templateExport(HttpServletResponse response, ITemplateExportDto req) throws Exception {
        ExcelInvokeCore.templateExport(response, req);
    }
}
