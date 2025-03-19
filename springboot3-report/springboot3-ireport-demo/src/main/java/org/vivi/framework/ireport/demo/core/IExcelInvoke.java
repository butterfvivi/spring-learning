package org.vivi.framework.ireport.demo.core;

import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ireport.demo.core.achieve.IExcelInvokeCore;
import org.vivi.framework.ireport.demo.common.base.IocUtil;
import org.vivi.framework.ireport.demo.core.dto.IDynamicExportDto;
import org.vivi.framework.ireport.demo.core.dto.ITemplateExportDto;

public class IExcelInvoke {

    private static IExcelInvokeCore IExcelInvokeCore = IocUtil.getBean(IExcelInvokeCore.class);

    /**
     * 动态导出
     * @param response
     * @param req
     */
    public static void dynamicExport(HttpServletResponse response, IDynamicExportDto req) throws Exception {
        IExcelInvokeCore.dynamicExport(response, req);
    }

    /**
     * 模板导出
     * @param response
     * @param req
     */
    public static void templateExport(HttpServletResponse response, ITemplateExportDto req) throws Exception {
        IExcelInvokeCore.templateExport(response, req);
    }
}
