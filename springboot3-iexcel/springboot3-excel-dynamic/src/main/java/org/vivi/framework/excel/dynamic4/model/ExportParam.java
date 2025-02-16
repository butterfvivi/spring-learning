package org.vivi.framework.excel.dynamic4.model;

import lombok.Data;

import java.util.List;

@Data
public class ExportParam {

    //参数1
    private String param1;

    //参数1
    private String param2;

    //动态可选择参数
    private List<String> choiceParamData;
}
