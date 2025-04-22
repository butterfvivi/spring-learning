package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class Report {

    private Long id;

    private String reportName;

    private Integer reportType;

    private String reportService;

    private String templateSet;

    private String reportGroup;

    private Integer groupOrder;

    private String groupName;

    private Integer reportOrder;

    private String reportDesc;
}
