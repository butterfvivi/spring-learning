package org.vivi.framework.report.simple.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.vivi.framework.report.simple.web.dto.ReportExcelDto;

import java.io.IOException;


public interface ReportExcelService  {

    /**
     * 根据报表编码查询详情
     *
     * @param reportCode
     * @return
     */
    ReportExcelDto detailByReportCode(String reportCode);
//
//    /**
//     * 报表预览
//     *
//     * @param reportExcelDto
//     * @return
//     */
//    ReportExcelDto preview(ReportExcelDto reportExcelDto);

    /**
     * 导出为excel
     *
     * @param reportExcelDto
     * @return
     */

    ResponseEntity<byte[]> exportExcel(HttpServletRequest request, HttpServletResponse response, ReportExcelDto reportExcelDto) throws IOException;

}
