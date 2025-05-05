package org.vivi.framework.ireport.demo.web.dto;

import lombok.Data;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;

import java.util.List;
import java.util.Map;

@Data
public class DataSearchDto {

    /**
     * report id
     */
    private Long setId;

    /**
     * head list
     */
    private List<String> headList;

    /**
     * export config
     */
    private IExportConfig exportConfig;

    /**
     * 额外参数
     */
    private Map<String, Object> params;

}
