package org.vivi.framework.ireport.demo.model.excel;

import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * export configuration
 */
@Data
public class IExportConfig {

    private String sheetName;

    private String targetParam;

    /**
     * merge column index, merge 1,2 columns, input [0,1]
     */
    private Set<Integer> mergeColIndex;

    /**
     * merge column row limit, merge column from the Nth row, if not specified, it will start merging from the header
     */
    private Integer mergerRowIndexLimit;

    /**
     * merge column limit, starting from mergerColIndex[0]. The logic is if the column value is: col1, col1, col1.clo2, col3,
     * then the limit is 2 for the first time, that is, between col1 and clo2
     */
    private Integer mergerColIndexLimit;

    /**
     * maximum number of words in the header. The width of all subsequent headers will be the same as its
     */
    private Integer headerWord;

    /**
     * custom excel interceptor handler
     */
    private List<WriteHandler> WriteHandlers;

    /**
     * custom excel cell interceptor handler
     */
    private Map<String,List<FillConfig>> fillConfigs;

    /**
     * template export, if column merge is required, non-header, when {} variable appears, the current row needs to be excluded, otherwise it may be merged
     */
    private Boolean excludeTillRow;

    /**
     * template export, if column merge is required, exclude the last row
     */
    private Set<Integer> excludeRowIndex;

    /**
     * whether the merge depends on the left row
     */
    private Boolean isNeedLeftConditionMerge;
}
