package org.vivi.framework.excel.configure.core.service;

import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;

import java.util.List;
import java.util.Map;

/**
 * 批量对象过滤器
 */
@Component
public interface IObjectBatchConverter extends IBatchConverter {
    /**
     * 进行批量转换
     * @param resultDataList 底层数据查询的返回结果
     * @param metaFieldMap   数据注解模型元数据
     * @param filterFieldBean   数据过滤上下文对象
     */
    void convertBatchFieldData(List resultDataList, Map<Integer, ExportFieldBean> metaFieldMap, ConverterFieldBean filterFieldBean) throws Exception;

}