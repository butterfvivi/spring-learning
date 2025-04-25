package org.vivi.framework.ireport.demo.web.dto;

import lombok.Data;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;

import java.util.Map;

@Data
public class DataSearchDto {

    /**
     * report id
     */
    private Long rtId;

    /**
     * export config
     */
    private IExportConfig exportConfig;

    /**
     * 动态参数
     */
    private Map<String, Object> searchData;

}
