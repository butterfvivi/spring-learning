package org.vivi.framework.report.simple.modules.datasettransform.service;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;

import java.util.List;

public interface TransformStrategy {
    /**
     * 数据清洗转换 类型
     * @return
     */
    String type();

    /***
     * 清洗转换算法接口
     * @param def
     * @param data
     * @return
     */
    List<JSONObject> transform(DataSetTransformDto def, List<JSONObject> data);
}
