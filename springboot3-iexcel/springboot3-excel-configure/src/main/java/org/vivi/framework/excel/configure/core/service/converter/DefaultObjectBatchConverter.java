package org.vivi.framework.excel.configure.core.service.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;
import org.vivi.framework.excel.configure.core.service.IObjectBatchConverter;
import org.vivi.framework.excel.configure.core.service.common.ExportHelperService;

import java.util.List;
import java.util.Map;

/**
 * 默认的数据过滤
 */
@Component("defaultObjectBatchConverter")
public class DefaultObjectBatchConverter implements IObjectBatchConverter {
    @Autowired
    private ExportHelperService exportHelperService;
    @Override
    public void convertBatchFieldData(List resultDataList, Map<Integer, ExportFieldBean> metaFieldMap, ConverterFieldBean filterFieldBean) throws Exception {

        for (Object object : resultDataList) {
            exportHelperService.getConvertRowDataObject(object, filterFieldBean);
        }
    }
}
