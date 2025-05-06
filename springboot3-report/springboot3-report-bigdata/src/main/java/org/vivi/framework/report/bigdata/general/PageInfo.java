package org.vivi.framework.report.bigdata.general;

import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {

    // 当前页的数据列表
    private List<T> list;

    // 总记录数
    private long total;

    // 当前页码（从1开始）
    private int pageNum;

    // 每页大小
    private int pageSize;

    // 是否有下一页
    private boolean hasNextPage;
}
