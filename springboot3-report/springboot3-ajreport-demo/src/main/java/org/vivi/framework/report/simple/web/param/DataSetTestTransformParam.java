/**/
package org.vivi.framework.report.simple.web.param;

import lombok.Data;
import org.vivi.framework.report.simple.modules.datasetparam.entity.dto.DataSetParamDto;
import org.vivi.framework.report.simple.modules.datasettransform.entity.dto.DataSetTransformDto;

import java.io.Serializable;
import java.util.List;


/**
* @desc DataSet 数据集查询输入类
**/
@Data
public class DataSetTestTransformParam implements Serializable{

    /** 数据源编码 */
    private String sourceCode;

    /** 动态查询sql或者接口中的请求体 */
    private String dynSentence;

    /** 数据集类型 */
    private String setType;

    /** 请求参数集合 */
    private List<DataSetParamDto> dataSetParamDtoList;

    /** 数据转换集合 */
    private List<DataSetTransformDto> dataSetTransformDtoList;

}
