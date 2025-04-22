package org.vivi.framework.ireport.demo.service.reportset;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.service.reportset.dto.ReportPreviewData;

public interface ReportSetService extends IService<Report> {
    /**
     * 获取报表设置
     *
     * @param id 报表ID
     * @return 报表设置
     */
    ReportPreviewData getReportSet(Integer id);

}
