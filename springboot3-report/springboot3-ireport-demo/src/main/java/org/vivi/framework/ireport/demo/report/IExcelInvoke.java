package org.vivi.framework.ireport.demo.report;

import com.alibaba.excel.write.handler.WriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.ireport.demo.common.style.AdaptiveWidthStyleStrategy;
import org.vivi.framework.ireport.demo.common.style.DefaultCellStyleUtils;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;
import org.vivi.framework.ireport.demo.web.request.ITemplateExportDto;
import org.vivi.framework.ireport.demo.web.request.ImportExcelDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class IExcelInvoke {


    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);


    /**
     * 导入数据解析，要求必须有一个实体类，目的是为了便于后期人员维护，和解析数据方便
     *
     * @param file
     * @param dto  {"targetParam":"","headRow":头部占几行}
     * @return 返回数据取决于用户自己定义
     */

    public Object importExcel(MultipartFile file, ImportExcelDto dto) throws Exception {
        return ExcelInvokeCore.importExcel(file, dto);
    }

    /**
     * 动态导出
     * @param response
     * @param req
     */
    public void dynamicExport(HttpServletResponse response, IDynamicExportDto req) throws Exception {
//        GenerateReportDto reportDto = req.getReportDto();
//        //get dataList
//        List<Map<String, Object>> allData = dataSetService.getAllData(reportDto);
//        req.setDataList(allData);
//
//        //get headList
//        ReportSetting dataset = dataSetService.getById(reportDto.getId());
//        String headers = dataset.getDynHeader();
//        List<String> headerList = Lists.newArrayList(headers.split(","));
//        req.setHeadList(headerList);
        //配置自定义样式，自适应宽度
        List<WriteHandler> writeHandlers = new ArrayList<>();
        writeHandlers.add(new AdaptiveWidthStyleStrategy());
        writeHandlers.add(DefaultCellStyleUtils.getHorizontalCellStyleStrategy());
        //writeHandlers.add(new CustomCellWriteHandler());
        IExportConfig iExportConfig = new IExportConfig();
        iExportConfig.setWriteHandlers(writeHandlers);
        //BeanUtils.copyProperties(req.getConfig(), iExportConfig);

        req.setConfig(iExportConfig);


        ExcelInvokeCore.dynamicExport(response, req);
    }

    /**
     * 模板导出
     * @param response
     * @param req
     */
    public void templateExport(HttpServletResponse response, ITemplateExportDto req) throws Exception {
        ExcelInvokeCore.templateExport(response, req);
    }
}
