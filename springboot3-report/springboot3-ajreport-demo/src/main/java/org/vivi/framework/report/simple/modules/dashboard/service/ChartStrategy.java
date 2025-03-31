package org.vivi.framework.report.simple.modules.dashboard.service;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.simple.web.dto.ChartDto;

import java.util.List;

public interface ChartStrategy {

    /**
     * 图表类型
     * @return
     */
    String type();

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @return
     */
    Object transform(ChartDto dto, List<JSONObject> data);
}
