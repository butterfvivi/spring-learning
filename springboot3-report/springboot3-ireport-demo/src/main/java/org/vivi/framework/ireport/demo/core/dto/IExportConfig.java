package org.vivi.framework.ireport.demo.core.dto;

import com.alibaba.excel.write.handler.WriteHandler;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * export configuration
 */
@Data
public class IExportConfig {

    private String targetParam;

    /**
     * merge column index, merge 1,2 columns, input [0,1]
     */
    private Set<Integer> mergeColIndex;

    /**
     * custom excel interceptor handler
     */
    private List<WriteHandler> WriteHandlers;

    /**
     * template export, if column merge is required, non-header, when {} variable appears, the current row needs to be excluded, otherwise it may be merged
     */
    private Boolean excludeTillRow;

    /**
     * template export, if column merge is required, exclude the last row
     */
    private Set<Integer> excludeRowIndex;
}
