package org.vivi.framework.report.bigdata.paging1.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Page {

    private Long startIndex;

    private Long pageSize;

}
