package org.vivi.framework.ireport.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.vivi.framework.ireport.demo.model.PageEntity;
import org.vivi.framework.ireport.demo.model.report.ReportSetting;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.List;
import java.util.Map;

public interface DataSetService extends IService<ReportSetting> {

    PageEntity getDatas(GenerateReportDto reportDto);

    List<Map<String, Object>> getAllData(GenerateReportDto reportDto);

    List<Map<String, Object>> getColumnInfos(GenerateReportDto reportDto);
}
