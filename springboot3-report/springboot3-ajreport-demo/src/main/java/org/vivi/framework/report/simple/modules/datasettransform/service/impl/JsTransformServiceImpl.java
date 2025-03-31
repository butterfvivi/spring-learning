package org.vivi.framework.report.simple.modules.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;
import org.vivi.framework.report.simple.modules.datasettransform.service.TransformStrategy;
import org.vivi.framework.report.simple.utils.JsEngineUtil;

import java.util.List;

/**
 * Created by raodeming on 2021/3/23.
 */
@Component
@Slf4j
public class JsTransformServiceImpl implements TransformStrategy {
    /**
     * 数据清洗转换 类型
     *
     * @return
     */
    @Override
    public String type() {
        return "js";
    }

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    @Override
    public List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data) {
        return getValueFromJs(def, data);
    }

    @Resource
    private JsEngineUtil jsEngineUtil;

    private List<JSONObject> getValueFromJs(DataSetTransformDto def, List<JSONObject> data) {
        String js = def.getTransformScript();
        try {
            return jsEngineUtil.eval(js, data);
        } catch (Exception ex) {
            log.info("执行js异常", ex);
            throw BusinessExceptionBuilder.build("500", ex.getMessage());
        }
    }
}
