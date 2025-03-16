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
public class PieChartServiceImpl implements ChartStrategy {
    /**
     * 图表类型
     *
     * @return
     */
    @Override
    public String type() {
        return "widget-piechart|widget-hollow-piechart|widget-funnel";
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

        return data;
    }

/*    [
        {
            "value": 11,
                "name": "指标1"
        },
        {
            "value": 10,
                "name": "指标2"
        }
    ]*/



}
