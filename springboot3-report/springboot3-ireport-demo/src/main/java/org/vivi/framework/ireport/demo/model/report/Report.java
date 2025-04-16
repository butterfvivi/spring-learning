package org.vivi.framework.ireport.demo.model.report;

import lombok.Data;

@Data
public class Report {

    private Long id;

    private String rtName;

    private Integer rtType;

    private String rtStrategy;

    private String templateSet;

    private String rtGroup;

    private Integer groupOrder;

    private String groupName;

    private Integer rtOrder;

    private String rtDesc;
}
