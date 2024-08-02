package org.vivi.framework.iexcelsimple.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iexcelsimple.common.exception.BizException;
import org.vivi.framework.iexcelsimple.common.utils.ReflectUtil;

/**
 * 枚举类型转换器
 */
@Slf4j
public class EnumColumConvert implements Converter<Object> {


    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Object convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        EnumFormat annotation = getAnnotation(contentProperty);
        if (!annotation.columnCode().isEmpty() && !annotation.columnName().isEmpty()) {
            return getEnumCode(cellData.getStringValue(), annotation);
        }
        return null;
    }

    @Override
    public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        if (object == null) {
            return new WriteCellData<>("");
        }
        EnumFormat annotation = getAnnotation(contentProperty);
        if (!annotation.columnCode().isEmpty() && !annotation.columnName().isEmpty()) {
            return new WriteCellData<>(getEnumValue(object, annotation));
        }
        return new WriteCellData<>("");
    }

    /**
     * 获取code
     *
     * @param descValue
     * @return
     */
    private Object getEnumCode(String descValue, EnumFormat annotation) {
        try {
            String columnName = annotation.columnName();
            String columnCode = annotation.columnCode();

            Class<? extends Enum> type = annotation.value();
            for (Enum<?> enumConstant : type.getEnumConstants()) {
                Object desc = ReflectUtil.invokeGet(enumConstant, columnName);
                Object code = ReflectUtil.invokeGet(enumConstant, columnCode);
                if (desc != null && descValue.equals(String.valueOf(desc))) {
                    return code;
                }
            }
        } catch (Exception e) {
            log.warn("获取枚举属性值失败", e);
            throw new BizException("获取枚举属性值失败" + descValue);
        }
        throw new BizException("未找到对应的值" + descValue);
    }


    /**
     * 导出：获取value值
     *
     * @param object
     * @return
     */
    private String getEnumValue(Object object, EnumFormat annotation) {
        try {
            String columnName = annotation.columnName();
            String columnCode = annotation.columnCode();
            Class<? extends Enum> type = annotation.value();
            for (Enum<?> enumConstant : type.getEnumConstants()) {
                Object descValue = ReflectUtil.invokeGet(enumConstant, columnName);
                Object code = ReflectUtil.invokeGet(enumConstant, columnCode);
                if (descValue != null && object.equals(code)) {
                    return String.valueOf(descValue);
                }
            }
        } catch (Exception e) {
            log.warn("获取枚举属性值失败", e);
            throw new BizException("获取枚举属性值失败" + object);
        }
        throw new BizException("获取枚举属性值失败" + object);
    }


    /**
     * 获取注解对象
     *
     * @param contentProperty
     * @return
     */
    private static EnumFormat getAnnotation(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getAnnotation(EnumFormat.class);
    }
}
