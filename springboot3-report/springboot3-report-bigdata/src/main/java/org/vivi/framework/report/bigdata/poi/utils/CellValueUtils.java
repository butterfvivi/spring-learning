package org.vivi.framework.report.bigdata.poi.utils;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;

import org.vivi.framework.report.bigdata.common.exception.BaseRuntimeException;
import org.vivi.framework.report.bigdata.common.exception.ErrorCode;
import org.vivi.framework.report.bigdata.poi.model.HeaderInfo;
import org.vivi.framework.report.bigdata.poi.model.ValidRules;
import org.vivi.framework.report.bigdata.utils.ClassUtils;
import org.vivi.framework.report.bigdata.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellValueUtils {

    private static final String TAIL_ZERO_REGEX = "^[0-9]+.[0]+$";
    private static final String TAIL_ZERO_REGEX_REPLACE = "\\.[0]+";
    /**
     * 根据单元格类型，设置单元格的值
     *
     * @param cell  单元格对象
     * @param value 单元格值
     */
    public static void setCellValueByType(Cell cell, Object value) {
        if (null == value) {
            return;
        }
        if (value instanceof Integer) {
            cell.setCellValue(((Integer) value));
        } else if (value instanceof Long) {
            cell.setCellValue(((Long) value));
        } else if (value instanceof Float) {
            setDefaultCellFormat(cell);
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            setDefaultCellFormat(cell);
            cell.setCellValue((Double) value);
        } else if (value instanceof Byte) {
            cell.setCellValue(((Byte) value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof BigDecimal) {
            setDefaultCellFormat(cell);
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Date) {
            CellStyle cellStyle = cell.getCellStyle();
            DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
            cellStyle.setDataFormat(format.getFormat(DateUtils.Parttern.FORMAT_YYMMDDHMS_MID.getFmt()));
            cell.setCellValue((Date) value);
        }
    }

    /**
     * 根据单元格类型，设置单元格的值
     *
     * @param cell  单元格对象
     * @param value 单元格值
     */
    public static void setCellValueByType(Cell cell, HeaderInfo headerInfo, Object value) {
        if ( null == value ) {
            return;
        }

        Class<?> clazz = headerInfo.getType();
        if( null == clazz || Object.class.equals(clazz) ){
            setValueByValueType(cell, headerInfo, value);
            return;
        }
        if(ClassUtils.isNumberType(clazz) && ClassUtils.isNumberType(value.getClass())){
            cell.setCellValue(((Number)value).doubleValue());
            return;
        }
        if( clazz == Date.class && StringUtils.isNumeric(String.valueOf(value)) ){
            setDateValueByType(cell, headerInfo, value);
            return;
        }
        setValueByValueType(cell, headerInfo, value);
    }

    /**设置日期格式**/
    private static void setDateValueByType(Cell cell, HeaderInfo headerInfo, Object value) {
        DateTime time = null;
        if( String.valueOf(value).length() == 10 ){
            time = DateUtils.create(Long.valueOf(String.valueOf(value)) * 1000);
        }
        if( String.valueOf(value).length() == 13 ){
            time = DateUtils.create(Long.valueOf(String.valueOf(value)));
        }
        if( null == time ){ return; }
        String format = headerInfo.getFormat();
        if(StringUtils.isBlank(format)){
            format = DateUtils.Parttern.FORMAT_YYMMDDHMS_MID.getFmt();
        }
        cell.setCellValue(DateUtil.format(time,format));
    }

    /**根据值类型设置单元格的值**/
    private static void setValueByValueType(Cell cell, HeaderInfo headerInfo, Object value) {
        if (value instanceof Integer) {
            cell.setCellValue(((Integer) value));
        } else if (value instanceof Long) {
            cell.setCellValue(((Long) value));
        } else if (value instanceof Float) {
            setCellFormat(cell, headerInfo);
            cell.setCellValue((Float) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
            setCellFormat(cell, headerInfo);
        } else if (value instanceof Byte) {
            cell.setCellValue(((Byte) value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof BigDecimal) {
            setCellFormat(cell, headerInfo);
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Date) {
            setCellFormat(cell, headerInfo);
            cell.setCellValue((Date) value);
        }
    }

    /**
     * 获取单元格的值
     **/
    public static Object getCellValueByType(Cell cell, HeaderInfo headerInfo) {
        if( null == cell ){ return null; }
        if (cell.getCellType().equals(CellType.BLANK)) {
            return null;
        }
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return transferExcelNumeric(cell, headerInfo);
        }
        if (cell.getCellType().equals(CellType.STRING)) {
            return transferStringByType(cell, headerInfo);
        }
        if (cell.getCellType().equals(CellType.FORMULA)) {
            return cell.getCellFormula();
        }
        if (cell.getCellType().equals(CellType.BOOLEAN)) {
            return cell.getBooleanCellValue();
        }
        if (cell.getCellType().equals(CellType.ERROR)) {
            return cell.getErrorCellValue();
        }
        return cell.getStringCellValue();
    }

    /**
     * Excel数字格式,转换为指定java基础数据类型
     **/
    private static Object transferExcelNumeric(Cell cell, HeaderInfo headerInfo) {
//        if (HSSFDateUtil.isCellDateFormatted(cell)) {
//            return DateUtils.format(
//                    DateUtils.create(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).getTime()),
//                    DateUtils.Parttern.FORMAT_YYMMDD_MID
//            );
//        }

        if( null == headerInfo ){
            if( null == cell ){ return null; }
            return cell.getNumericCellValue();
        }

        Number number;
        if (StringUtils.isNotBlank(headerInfo.getFormat())
                && headerInfo.getFormat().matches(ValidRules.REGEX_NUMBER)) {
            DecimalFormat df = new DecimalFormat(headerInfo.getFormat());
            RoundingMode roundingMode = headerInfo.getMode();
            if (null != roundingMode) {
                df.setRoundingMode(headerInfo.getMode());
            }
            number = new BigDecimal(df.format(cell.getNumericCellValue()));
        } else {
            number = cell.getNumericCellValue();
        }

        return transferNumricByType(number, headerInfo);
    }

    /**
     * 根据对象基本数据类型,获取单元格的值
     **/
    private static Object transferStringByType(Cell cell, HeaderInfo headerInfo) {
        String value = cell.getStringCellValue();
        if( null == headerInfo ){
            // 默认的处理方式:如果是整数后边带了.0后缀,则自动去掉
            return cutZeroTail(value);
        }

        Class<?> clazz = headerInfo.getType();
        if( null == clazz || Object.class.equals(clazz) ){
            return cutZeroTail(value);
        }

        if (StringUtils.isNotBlank(headerInfo.getRegex())
                && !value.matches(headerInfo.getRegex())) {
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),headerInfo.getTitle() + "格式错误");
        }

        Object result = value;
        if (boolean.class == clazz || Boolean.class == clazz) {
            return Boolean.valueOf(value);
        } else if (String.class == clazz){
            return cutZeroTail(value);
        }

        if( ClassUtils.isIntegerType(clazz) ){
            return Double.valueOf(value).longValue();
        }

        if( ClassUtils.isDecimalType(clazz) ){
            result = Double.valueOf(value);
            if (StringUtils.isNotBlank(headerInfo.getFormat())
                    && headerInfo.getFormat().matches(ValidRules.REGEX_NUMBER)) {
                result = formatDecimal(headerInfo, value);
            }
        }
        return result;
    }

    /**格式化浮点数类型**/
    private static Object formatDecimal(HeaderInfo headerInfo, String value) {
        Object result;
        DecimalFormat df = new DecimalFormat(headerInfo.getFormat());
        if (null != headerInfo.getMode()) {
            df.setRoundingMode(headerInfo.getMode());
        }
        Number number = df.parse(value, new ParsePosition(0)).doubleValue();
        result = new BigDecimal(df.format(number));
        return result;
    }

    /**
     * 根据对象基本数据类型,获取单元格的值
     **/
    private static Object transferNumricByType(Number number, HeaderInfo headerInfo) {
        Object result = null;
        Class<?> clazz = headerInfo.getType();
        if( null == clazz || Object.class.equals(clazz)){
            return cutZeroTail(number.toString());
        }

        if( ClassUtils.isNumberType(clazz) ){
            return number.doubleValue();
        }

        if (boolean.class == clazz || Boolean.class == clazz) {
            result = number.intValue() == 0;
        } else if (clazz == String.class) {
            result = cutZeroTail(number.toString());
        }
        return result;
    }

    private static String cutZeroTail(String result) {
        if (result.matches(TAIL_ZERO_REGEX)) {
            result = result.replaceAll(TAIL_ZERO_REGEX_REPLACE, "");
        }
        return result;
    }

    /**设置单元格内容格式化**/
    private static void setCellFormat(Cell cell, HeaderInfo headerInfo) {
        String format = headerInfo.getFormat();
        // 如果设置了自定义格式,则不再设置默认的数据格式
        if (StringUtils.isBlank(format)) {
            CellStyle cellStyle = cell.getCellStyle();
            DataFormat dataFormat = cell.getSheet().getWorkbook().createDataFormat();
            if( StringUtils.isNotBlank(headerInfo.getFormat()) ){
                cellStyle.setDataFormat(dataFormat.getFormat(headerInfo.getFormat()));
            }
        }
    }

    /**设置单元格内容默认格式**/
    public static void setDefaultCellFormat(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
        cellStyle.setDataFormat(format.getFormat("0.00"));
    }
}
