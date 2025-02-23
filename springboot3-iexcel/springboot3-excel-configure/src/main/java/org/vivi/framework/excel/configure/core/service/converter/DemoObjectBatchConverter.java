package org.vivi.framework.excel.configure.core.service.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.vivi.framework.excel.configure.core.beans.ConverterFieldBean;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;
import org.vivi.framework.excel.configure.core.service.IObjectBatchConverter;

import java.util.List;
import java.util.Map;

@Component("demoObjectBatchConverter")
public class DemoObjectBatchConverter implements IObjectBatchConverter {
    private Logger logger = LoggerFactory.getLogger(DemoObjectBatchConverter.class);

    @Override
    public void convertBatchFieldData(List resultDataList, Map<Integer, ExportFieldBean> metaFieldMap, ConverterFieldBean filterFieldBean) throws Exception {
        logger.info("exe myDemoObjectBatchConverter >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
