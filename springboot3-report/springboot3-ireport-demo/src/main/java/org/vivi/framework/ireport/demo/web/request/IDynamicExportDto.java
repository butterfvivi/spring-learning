package org.vivi.framework.ireport.demo.web.request;

import lombok.Data;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.List;
import java.util.Map;

@Data
public class IDynamicExportDto {

    private GenerateReportDto reportDto;
    /**
     * export data list
     */
    private List dataList;

    /**
     * export head list
     */
    private List<String> headList;

    /**
     * exported configuration
     */
    private IExportConfig config;


    /**
     * additional parameters
     */
    private Map<String, Object> params;
}
