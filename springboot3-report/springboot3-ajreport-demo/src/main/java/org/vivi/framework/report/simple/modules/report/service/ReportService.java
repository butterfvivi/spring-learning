package org.vivi.framework.report.simple.modules.report.service;

import org.vivi.framework.report.simple.web.dto.ReportDto;

public interface ReportService {


    /**
     * 下载次数+1
     * @param reportCode
     */
    void downloadStatistics(String reportCode);

    /**
     * 复制大屏
     * @param dto
     */
    void copy(ReportDto dto);
}
