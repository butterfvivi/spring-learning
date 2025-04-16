package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class ReportSheetSetting {

    private Long id;

    private Long setId;

    private String sheetName;

    private Integer sheetOrder;

    private String titleName;

    private String dynHeader;

    private String styleService;

    private String calFormula;

    private String sheetConfig;

}
