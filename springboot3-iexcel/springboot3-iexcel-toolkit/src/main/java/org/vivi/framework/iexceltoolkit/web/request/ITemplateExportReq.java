package org.vivi.framework.iexceltoolkit.web.request;

import lombok.Data;
import org.vivi.framework.iexceltoolkit.toolkit.dto.IExportConfig;

import java.util.List;
import java.util.Map;

@Data
public class ITemplateExportReq {

    //模板路径---如果是模板导出必填
    private String templatePath;

    //渲染list数据
    //导出的数据
    private List dataList;

    //头部数据，也可以包含一些查询条件等数据
    private Map<String, Object> otherVal;

    //配置项目
    private IExportConfig config;

    //扩展：mutiple table data 填充。
    private Map<String,List> mutipleTableDatas;
}
