package org.vivi.framework.ireport.demo.service.dataset;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.dataset.ReportDataSet;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.dto.ReportPageDto;

import java.util.List;
import java.util.Map;

public interface DataSetService extends IService<ReportDataSet> {

    List<ReportDataSet> getAllReportDatas(Long reportId);

    PageEntity getPageData(ReportPageDto reportPageDto);

    List<Map<String, Object>> getAllMapData(GenerateReportDto reportDto);

    List<Map<String, Object>> getColumnInfos(ReportPageDto reportPageDto);

}
