package org.vivi.framework.report.simple.modules.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;
import org.vivi.framework.report.simple.modules.datasettransform.service.TransformStrategy;

import java.util.List;
import java.util.Set;

/**
 * 字典转换
 */
@Component
@Slf4j
public class DictTransformServiceImpl implements TransformStrategy {

    /**
     * 数据清洗转换 类型
     */
    @Override
    public String type() {
        return "dict";
    }

    /***
     * 清洗转换算法接口
     */
    @Override
    public List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data) {
        String transformScript = def.getTransformScript();
        if (StringUtils.isBlank(transformScript)) {
            return data;
        }
        JSONObject jsonObject = JSONObject.parseObject(transformScript);
        Set<String> keys = jsonObject.keySet();

        data.forEach(dataDetail -> dataDetail.forEach((key, value) -> {
            if (keys.contains(key)) {
                String string = jsonObject.getJSONObject(key).getString(value.toString());
                if (StringUtils.isNotBlank(string)) {
                    dataDetail.put(key, string);
                }
            }
        }));
        return data;
    }
}
