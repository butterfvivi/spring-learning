
package org.vivi.framework.ireport.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportMapper;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.service.ReportDataStrategy;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.request.IDynamicExportDto;

import java.util.HashMap;
import java.util.Map;

/**
* DataSetTransform 数据集数据转换服务实现
**/
@Service
public class ReportDataServiceImpl {

    private final Map<String, ReportDataStrategy> queryServiceImplMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private ReportMapper reportMapper;

    public ReportDataStrategy getTarget(String type) {
        return queryServiceImplMap.get(type);
    }

    //@Override
    public IDynamicExportDto transform(GenerateReportDto reportDto, IDynamicExportDto exportDto) {
        if (reportDto == null ) {
            return exportDto;
        }

        Report report = reportMapper.selectById(reportDto.getId());
        return getTarget(report.getRtStrategy()).transform(reportDto, exportDto);
    }
}
