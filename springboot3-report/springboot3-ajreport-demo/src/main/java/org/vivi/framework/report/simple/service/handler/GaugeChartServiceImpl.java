package org.vivi.framework.report.simple.service.handler;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.simple.strategy.ChartStrategy;
import org.vivi.framework.report.simple.web.dto.ChartDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 饼图或者空心饼图或者漏斗图
 */
@Component
public class GaugeChartServiceImpl implements ChartStrategy {
    /**
     * 图表类型
     *
     * @return
     */
    @Override
    public String type() {
        return "widget-gauge";
    }

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @param data
     * @return
     */
    @Override
    public Object transform(ChartDto dto, List<JSONObject> data) {

//        return "{\"value\": 50, \"name\": \"名称1\", \"unit\": \"%\"}";
        return data;
    }




}
