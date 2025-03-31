
package org.vivi.framework.report.simple.modules.datasettransform.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;
import org.vivi.framework.report.simple.modules.datasettransform.dao.DataSetTransformMapper;
import org.vivi.framework.report.simple.modules.datasettransform.service.DataSetTransformService;
import org.vivi.framework.report.simple.modules.datasettransform.service.TransformStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @desc DataSetTransform 数据集数据转换服务实现
**/
@Service
public class DataSetTransformServiceImpl implements DataSetTransformService {

    private final Map<String, TransformStrategy> queryServiceImplMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private DataSetTransformMapper dataSetTransformMapper;

    public TransformStrategy getTarget(String type) {
        return queryServiceImplMap.get(type);
    }

    @Override
    public List<JSONObject> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data) {
        if (dataSetTransformDtoList == null || dataSetTransformDtoList.size() <= 0) {
            return data;
        }

        for (DataSetTransformDto dataSetTransformDto : dataSetTransformDtoList) {
            data = getTarget(dataSetTransformDto.getTransformType()).transform(dataSetTransformDto, data);
        }
        return data;
    }
}
