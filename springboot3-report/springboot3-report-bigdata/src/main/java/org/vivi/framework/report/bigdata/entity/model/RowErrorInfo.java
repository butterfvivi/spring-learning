package org.vivi.framework.report.bigdata.entity.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class RowErrorInfo {
    private Integer row;
    private List<String> columnErrors = Lists.newArrayList();
}
