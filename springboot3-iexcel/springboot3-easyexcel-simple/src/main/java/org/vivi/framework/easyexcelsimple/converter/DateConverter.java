package org.vivi.framework.easyexcelsimple.converter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zph
 * @date 2024/4/19 14:58
 */
public class DateConverter implements Converter<Date> {

    /**
     * date转换
     */
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * dateTime转换
     */
    static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Date convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        //根据时间类型来处理日期
        if (cellData.getType() == CellDataTypeEnum.NUMBER) {
            return HSSFDateUtil.getJavaDate(Double.parseDouble(StrUtil.toString(cellData.getNumberValue())));
        } else if (cellData.getType() == CellDataTypeEnum.STRING) {
            String value = cellData.getStringValue().replace("/", "-").replace(".", "-");
            Date date;
            try {
                if (value.length() <= 10) {
                    date = DATE_FORMAT.parse(value);
                } else {
                    date = DATE_TIME_FORMAT.parse(value);
                }
            } catch (Exception e) {
                throw new RuntimeException("日期时间格式错误 请修改为 'yyyy-MM-dd'或'yyyy-MM-dd HH:mm:ss' 格式");
            }
            return date;
        } else {
            return null;
        }
    }

    @Override
    public WriteCellData<String> convertToExcelData(Date value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(DATE_TIME_FORMAT.format(value));
    }

}

