package org.vivi.framework.ireport.demo.core.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IDynamicExportDto {

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
