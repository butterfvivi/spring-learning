package org.vivi.framework.ireport.demo.model.dataset;

import lombok.Data;

@Data
public class ReportDataSet {

    private Long id;

    private String rpId;

    private String dynSentence;

    private String setParam;

    private boolean isPatination;

    private Integer isCustomerPage = 2;

    private String page_count;
}
