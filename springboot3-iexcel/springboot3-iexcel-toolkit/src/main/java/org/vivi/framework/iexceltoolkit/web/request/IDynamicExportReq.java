package org.vivi.framework.iexceltoolkit.web.request;

import lombok.Data;
import org.vivi.framework.iexceltoolkit.toolkit.dto.IExportConfig;

import java.util.List;
import java.util.Map;

@Data
public class IDynamicExportReq {

    //导出的数据
    private List dataList;

    //头部数据----注意，如果顺序错乱，可以使用  姓名@name，表示姓名这列绑定的是name字段。
    private List<String> headList;

    //配置项目
    private IExportConfig config;

    //一些携带的查询条件参数等信息
    private Map<String, Object> params;
}
