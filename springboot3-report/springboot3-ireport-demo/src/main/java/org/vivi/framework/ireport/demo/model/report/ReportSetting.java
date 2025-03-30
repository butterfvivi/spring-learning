package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class ReportSetting {

    private Long id;

    private Long rpId;

    private String sheetName;

    private String titleName;

    private String dynHeader;

    private String headerMap;

    private String dynSentence;

    private String setParam;

    private boolean isPatination;

    private Integer isCustomerPage = 2;

    private Integer page_count;

    private Integer sqlType;
}
