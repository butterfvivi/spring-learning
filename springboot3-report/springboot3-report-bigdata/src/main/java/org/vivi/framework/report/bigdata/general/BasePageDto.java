package org.vivi.framework.report.bigdata.general;

import lombok.Data;

@Data
public class BasePageDto {

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 每页大小
     */
    private int pageSize = 10;
}
