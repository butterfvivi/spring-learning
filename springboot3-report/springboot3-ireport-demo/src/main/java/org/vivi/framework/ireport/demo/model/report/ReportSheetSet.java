package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class ReportSheetSet {

    private Long id;

    private Long rtId;

    private String sheetName;

    private Integer sheetOrder;

    private String titleName;

    private String dynHeader;

    private String styleService;

    private String calFormula;

    private String sheetConfig;

}
