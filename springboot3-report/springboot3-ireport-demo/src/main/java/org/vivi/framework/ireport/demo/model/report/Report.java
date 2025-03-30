package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class Report {

    private Long id;

    private String RptName;

    private String RpType;

    private String RpStrategy;

    private String TemplatePath;
}
