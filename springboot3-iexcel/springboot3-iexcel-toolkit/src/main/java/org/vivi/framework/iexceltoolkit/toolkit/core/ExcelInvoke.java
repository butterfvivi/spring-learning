package org.vivi.framework.iexceltoolkit.toolkit.core;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.utils.IocUtil;
import org.vivi.framework.iexceltoolkit.web.request.ImportReq;
import org.vivi.framework.iexceltoolkit.toolkit.achieve.ExcelInvokeCore;
import org.vivi.framework.iexceltoolkit.web.request.IDynamicExportReq;
import org.vivi.framework.iexceltoolkit.web.request.ITemplateExportReq;

public class ExcelInvoke {

    private static ExcelInvokeCore excelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);

    /**
     * 导入数据解析，要求必须有一个实体类，目的是为了便于后期人员维护，和解析数据方便
     *
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     */

    public static Object importExcel(MultipartFile file, ImportReq dto) throws Exception {
        return excelInvokeCore.importExcel(file, dto);
    }

    /**
     * 动态导出
     * @param response
     * @param req
     */
    public static void dynamicExport(HttpServletResponse response, IDynamicExportReq req) throws Exception {
        excelInvokeCore.dynamicExport(response, req);
    }

    /**
     * 模板导出
     * @param response
     * @param req
     */
    public static void templateExport(HttpServletResponse response, ITemplateExportReq req) throws Exception {
        excelInvokeCore.templateExport(response, req);
    }
}
