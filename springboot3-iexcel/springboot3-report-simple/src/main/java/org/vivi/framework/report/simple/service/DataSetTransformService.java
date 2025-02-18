
package org.vivi.framework.report.simple.service;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.simple.entity.datasettransform.dto.DataSetTransformDto;

import java.util.List;

/**
* @desc DataSetTransform 数据集数据转换服务接口
**/
public interface DataSetTransformService{

    List<JSONObject> transform(List<DataSetTransformDto> dataSetTransformDtoList, List<JSONObject> data);

}
