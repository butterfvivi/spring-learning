package org.vivi.framework.iexcel.common.context;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ReadContext implements Context {

    private Integer sheetNo;

    private String sheetName;

    private Object rowData;

    private Integer rowIndex;

    /**
     * total may be inaccurate
     *
     * {@see com.alibaba.excel.read.metadata.holder.ReadSheetHolder#approximateTotalRowNumber}
     */
    private Integer rowTotal;
}
