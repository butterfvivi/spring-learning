package org.vivi.framework.excel.configure.core.service.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.common.exceptions.NoConvertMappingCacheException;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;
import org.vivi.framework.excel.configure.core.service.IMapBatchConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的数据转换器
 */
@Component("defaultMapBatchConverter")
public class DefaultMapBatchConverter implements IMapBatchConverter {


    @Override
    public void convertBatchFieldData(List<Map<Integer, String>> resultDataMapList, Map<Integer, ExportFieldBean> metaFieldMap, ConverterFieldBean filterFieldBean) throws NoConvertMappingCacheException {
        for (Map<Integer, String> dataMap : resultDataMapList) {
            Map<Integer, String> tmpdataMap = new HashMap(dataMap);
            for (Map.Entry<Integer,String> entry : tmpdataMap.entrySet()){
                boolean b = StringUtils.isNotEmpty(entry.getValue()) && filterFieldBean.getSourceLinkedSet().contains(entry.getKey().toString());
                if (!b) {
                   continue;
                }

                ExportFieldBean exportFieldBean = metaFieldMap.get(entry.getKey());
                if(StringUtils.isEmpty(exportFieldBean.getSourceKey())){
                    continue;
                }

                Map<String, String> valueMap = filterFieldBean.getSourceLinkedMap().get(exportFieldBean.getSourceKey());
                if(valueMap == null){
                    throw new NoConvertMappingCacheException("the column " + entry.getKey() + "'s sourceKey is not null, but queryCacheMap is null !");
                }
                String referValue = valueMap.get(entry.getValue());
                if(StringUtils.isEmpty(referValue)){
                    referValue = "";
                }
                if (exportFieldBean.getReferIndex() > 0) {
                    if(filterFieldBean.getSourceLinkedMap() == null){
                        throw new NullPointerException(" the sourceKey:"+exportFieldBean.getSourceKey()+" not has a mapCache .");
                    }
                    dataMap.put(exportFieldBean.getReferIndex(), referValue);
                } else {
                    dataMap.put(entry.getKey(), referValue);
                }
            }
        }
    }
}
