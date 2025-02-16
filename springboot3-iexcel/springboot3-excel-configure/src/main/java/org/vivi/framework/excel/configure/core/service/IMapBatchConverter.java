package org.vivi.framework.excel.configure.core.service;

import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;

import java.util.List;
import java.util.Map;

/**
 * 批量map转换
 */
public interface IMapBatchConverter {

    /**
     * 批量转换
     * @param resultDataMapList  底层返回的结果数据集
     * @param metaFieldMap       数据注解模型中的元数据
     * @param filterFieldBean    过滤上下文对象
     */
    void convertBatchFieldData(List<Map<Integer, String>> resultDataMapList, Map<Integer, ExportFieldBean> metaFieldMap, ConverterFieldBean filterFieldBean) throws Exception;

}
