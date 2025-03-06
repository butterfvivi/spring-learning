package org.vivi.framework.easyexcel.simple.converter;

import com.alibaba.excel.converters.Converter;
import java.time.LocalDateTime;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zph
 * @date 2024/4/19 15:08
 */
public class LocalDateTimeConverter implements Converter<LocalDateTime> {


    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    public LocalDateTime convertToJavaData(ReadCellData<?> cellData) {
        if (CellDataTypeEnum.NUMBER.equals(cellData.getType())) {
            Date date = HSSFDateUtil.getJavaDate(cellData.getNumberValue().doubleValue());
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (CellDataTypeEnum.STRING.equals(cellData.getType())) {
            String value = cellData.getStringValue().replace("/", "-");
            if (value.length() <= 10) {
                value = value + " 00:00:00";
            }
            LocalDateTime dateTime;
            try {
                dateTime = LocalDateTime.parse(value, FORMATTER);
            } catch (Exception e) {
                throw new RuntimeException("日期时间格式错误 请修改为 'yyyy-MM-dd HH:mm:ss'格式");
            }
            return dateTime;
        } else {
            return null;
        }
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return LocalDateTime.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(DEFAULT_PATTERN));
    }

    @Override
    public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(value.format(FORMATTER));
    }

}
