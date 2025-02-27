package org.vivi.framework.report.simple.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.simple.common.exception.BusinessExceptionBuilder;
import org.vivi.framework.report.simple.entity.datasetparam.dto.DataSetParamDto;
import org.vivi.framework.report.simple.mapper.DataSetParamMapper;
import org.vivi.framework.report.simple.service.DataSetParamService;
import org.vivi.framework.report.simple.utils.JsEngineUtil;
import org.vivi.framework.report.simple.utils.ParamsResolverHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @desc DataSetParam 数据集动态参数服务实现
**/
@Service
@Slf4j
public class DataSetParamServiceImpl implements DataSetParamService {

    @Autowired
    private DataSetParamMapper dataSetParamMapper;

    @Resource
    private JsEngineUtil jsEngineUtil;
    /**
     * 参数替换
     *
     * @param contextData
     * @param dynSentence
     * @return
     */
    @Override
    public String transform(Map<String, Object> contextData, String dynSentence) {
        if (StringUtils.isBlank(dynSentence)) {
            return dynSentence;
        }
        if (dynSentence.contains("${")) {
            dynSentence = ParamsResolverHelper.resolveParams(contextData, dynSentence);
        }
        if (dynSentence.contains("${")) {
            throw BusinessExceptionBuilder.build("500", dynSentence);
        }
        return dynSentence;
    }

    /**
     * 参数替换
     *
     * @param dataSetParamDtoList
     * @param dynSentence
     * @return
     */
    @Override
    public String transform(List<DataSetParamDto> dataSetParamDtoList, String dynSentence) {
        Map<String, Object> contextData = new HashMap<>();
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() <= 0) {
            return dynSentence;
        }
        dataSetParamDtoList.forEach(dataSetParamDto -> {
            contextData.put(dataSetParamDto.getParamName(), dataSetParamDto.getSampleItem());
        });
        return transform(contextData, dynSentence);
    }

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDto
     * @return
     */
    @Override
    public Object verification(DataSetParamDto dataSetParamDto) {
        String validationRules = dataSetParamDto.getValidationRules();
        if (StringUtils.isNotBlank(validationRules)) {
            try {
                return jsEngineUtil.verification(validationRules,dataSetParamDto);
            } catch (Exception ex) {
                throw BusinessExceptionBuilder.build("500", ex.getMessage());
            }
        }
        return true;
    }

    /**
     * 参数校验  js脚本
     *
     * @param dataSetParamDtoList
     * @return
     */
    @Override
    public boolean verification(List<DataSetParamDto> dataSetParamDtoList, Map<String, Object> contextData) {
        if (null == dataSetParamDtoList || dataSetParamDtoList.size() == 0) {
            return true;
        }

        for (DataSetParamDto dataSetParamDto : dataSetParamDtoList) {
            if (null != contextData) {
                String value = contextData.getOrDefault(dataSetParamDto.getParamName(), "").toString();
                dataSetParamDto.setSampleItem(value);
            }

            Object verification = verification(dataSetParamDto);
            if (verification instanceof Boolean) {
                if (!(Boolean) verification) {
                    return false;
                }
            }else {
                //将得到的值重新赋值给contextData
                if (null != contextData) {
                    contextData.put(dataSetParamDto.getParamName(), verification);
                }
                dataSetParamDto.setSampleItem(verification.toString());
            }

        }
        return true;
    }

}
