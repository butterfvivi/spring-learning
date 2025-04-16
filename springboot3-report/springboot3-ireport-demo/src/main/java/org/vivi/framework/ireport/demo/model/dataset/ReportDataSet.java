package org.vivi.framework.ireport.demo.model.dataset;

import lombok.Data;

@Data
public class ReportDataSet {

    private Long id;

    private Integer rtId;

    private Integer sheetIndex;

    private String rtSql;

    private String setParams;

    private boolean isPatination;

    private Integer dataType;
}
