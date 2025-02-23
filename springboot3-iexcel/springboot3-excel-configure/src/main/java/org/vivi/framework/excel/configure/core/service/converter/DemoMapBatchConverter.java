package org.vivi.framework.excel.configure.core.service.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author fanchunshuai
 * @Date 2019/11/1 11
 * @Description:
 */
@Component("demoMapBatchConverter")
public class DemoMapBatchConverter implements Converter<HashMap<String, Object>> {

    @Override
    public Class<HashMap> supportJavaTypeKey() {
        return HashMap.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }


    /**
     * 这里是写的时候会调用。/
     */
    @Override
    public WriteCellData<HashMap<String,Object>> convertToExcelData(WriteConverterContext<HashMap<String, Object>> context) {

        return new WriteCellData<HashMap<String,Object>>(String.valueOf(context.getValue()));


    }
}
