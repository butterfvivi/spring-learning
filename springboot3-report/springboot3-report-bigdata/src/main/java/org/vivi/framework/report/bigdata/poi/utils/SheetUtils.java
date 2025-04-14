package org.vivi.framework.report.bigdata.poi.utils;


import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.util.CollectionUtils;
import org.vivi.framework.report.bigdata.poi.config.ExportConfig;
import org.vivi.framework.report.bigdata.common.exception.BaseRuntimeException;
import org.vivi.framework.report.bigdata.common.exception.ErrorCode;
import org.vivi.framework.report.bigdata.poi.model.*;
import org.vivi.framework.report.bigdata.utils.ClassUtils;
import org.vivi.framework.report.bigdata.utils.StringUtils;
import org.vivi.framework.report.bigdata.poi.regex.RegexUtils;
import org.vivi.framework.report.bigdata.common.annotation.Header;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 功能 :
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SheetUtils {

    /**
     * 获取excel-sheet表头
     **/
    public static Row getHeaderRow(Sheet sheet,List<String> titleNames){
        Integer firstRowNum = sheet.getFirstRowNum();
        Integer maxNum = firstRowNum + ValidRules.MAX_HEADER_ROW_NUM;
        Row headerRow = null;
        for (int j = firstRowNum; j < maxNum; j++) {
            Row row = sheet.getRow(j);
            if (null == row && j < maxNum - 1) { continue; }
            if (null == row) {
                throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"Excel有效行和表头之间间隔超过"+ ValidRules.MAX_HEADER_ROW_NUM+"行");
            }

            List<String> titles = Lists.newArrayList();
            boolean isAllTitleRight = readHeaderRow(titleNames, row, titles);
            if( titles.size() == titleNames.size() || isAllTitleRight){
                headerRow = row;
                break;
            }
        }
        if( null == headerRow ){
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"未获取到正确的表头,操作终止.");
        }
        return headerRow;
    }

    /**
     * 统一设置列宽
     *
     * @param sheet        表格sheet
     * @param columnWidths 列宽度
     * @param unionWith
     */
    public static void setColumnsWidth(Sheet sheet, Map<Integer, Integer> columnWidths, Integer unionWith) {
        if (null == sheet) { return; }

        if (unionWith > 0) {
            Integer totalWidth = 0;
            for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
                totalWidth += (entry.getValue());
            }
            if (totalWidth < unionWith && totalWidth > 0) {
                double rate = (unionWith / (double) (totalWidth));
                for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
                    sheet.setColumnWidth(entry.getKey(), (int) ((entry.getValue()) * ValidRules.DEFAULT_CELL_WIDTH_MULTIPLE * rate));
                }
                return;
            }
        }

        for (Map.Entry<Integer, Integer> entry : columnWidths.entrySet()) {
            if (ValidRules.DEFAULT_CELL_WIDTH_WIDTH < entry.getValue()) {
                sheet.setColumnWidth(entry.getKey(), (ValidRules.DEFAULT_CELL_WIDTH_WIDTH) * ValidRules.DEFAULT_CELL_WIDTH_MULTIPLE);
            } else {
                sheet.setColumnWidth(entry.getKey(), (entry.getValue()) * ValidRules.DEFAULT_CELL_WIDTH_MULTIPLE);
            }
        }
    }

    /**初始化title:从对象中获取Excel表头**/
    public static void setTitles(SheetInfo sheetInfo, ExportConfig config) {
        setTitles(sheetInfo,config.getClazz(),config);
    }

    /**初始化title:从对象中获取Excel表头**/
    public static void setTitles(SheetInfo sheetInfo, Class<?> clazz) {
        setTitles(sheetInfo,clazz,null);
    }

    /**从map中获取Excel表头**/
    public static void setTitles(SheetInfo sheetInfo, String titles) {
        Map<String,String> headerTitles = Maps.newHashMap();
        Map<String,Class> headerClasses = Maps.newHashMap();
        List<String> headerNames = Lists.newArrayList();

        transferTitles(headerTitles,headerClasses,headerNames,titles);

        for(String title : headerNames){
            String name = headerTitles.get(title);
            HeaderInfo headerInfo = new HeaderInfo();
            headerInfo.setName(name);
            headerInfo.setTitle(title);
            Class<?> clazz = headerClasses.get(title);
            if( null != clazz ){
                headerInfo.setType(clazz);
            }
            sheetInfo.getHeaderInfos().put(title, headerInfo);
            sheetInfo.getHeaderNames().add(title);
        }
    }

    /**初始化单个sheet的特殊表头**/
    public static void setSheetDescription(SheetInfo sheetInfo, CellStyle mergeTitleStyle) {

        if( sheetInfo.isUseTemplate() ){ return; }

        if( StringUtils.isBlank(sheetInfo.getDescription()) ){ return; }

        Sheet sheet = sheetInfo.getSheet();
        Integer rowNumber = sheetInfo.getUnionLine();
        Integer maxRowNum = sheetInfo.getMaxRowNum();
        Row row = sheetInfo.getSheet().createRow(rowNumber % maxRowNum);
        Cell headCell = row.createCell(0);
        headCell.setCellStyle(mergeTitleStyle);
        CellValueUtils.setCellValueByType(headCell, sheetInfo.getDescription());

        /*统计\n符号在文本中的个数**/
        List<String> colgroups = RegexUtils.matchsReturnChar(sheetInfo.getDescription());
        List<String> lines = Splitter.on("\n").trimResults().splitToList(sheetInfo.getDescription());
        /**计算合并单元格宽度**/
        Integer unionWidth = 0;
        for (String line : lines) {
            Integer length = line.length();
            if (length > unionWidth) {
                unionWidth = length;
            }
        }
        row.setHeight((short) (sheetInfo.getRowHeight() * (colgroups.size())));

        Integer length = sheetInfo.getHeaderNames().size();
        /*
         * 设定合并单元格区域范围
         *  firstRow  0-based
         *  lastRow   0-based
         *  firstCol  0-based
         *  lastCol   0-based
         */
        CellRangeAddress cra = new CellRangeAddress(rowNumber, rowNumber, 0, length - 1);
        sheet.addMergedRegion(cra);
        RegionUtil.setBorderBottom(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN,cra,sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN,cra,sheet);
        sheetInfo.setUnionWith(unionWidth);
        // 开始写数据的行加1
        sheetInfo.setBeginRow(sheetInfo.getBeginRow()+1);
    }

    /**设置单元格下拉框值**/


    /**执行载入**/
    public static void load(SheetInfo sheetInfo){
        if( null == sheetInfo.getClazz()
                && CollectionUtils.isEmpty(sheetInfo.getHeaderNames())
                && !TitleType.NONE.equals(sheetInfo.getTitleType())){
            return;
        }
        if( !TitleType.NONE.equals(sheetInfo.getTitleType()) ){
            sheetInfo.setHeaderRow(SheetUtils.getHeaderRow(sheetInfo.getSheet(),sheetInfo.getHeaderNames()));
        }
        try {
            fetchDataFromExcel(sheetInfo);
        } catch (Exception e) {
            throw new BaseRuntimeException(ErrorCode.EXPORT_ERROR.getCode(),"导入:导入数据失败:"+e.getMessage());
        }
    }

    /**导出**/
    public static void export(SheetInfo sheetInfo, CellStyle headerStyle) {
        writeExcelTitleDatas(sheetInfo,headerStyle);
        writeExcelRowDatas(sheetInfo);
    }

    /*
     ****************************************私有方法区*******************************************
                   _               _                           _    _                 _
                  (_)             | |                         | |  | |               | |
      _ __   _ __  _ __   __ __ _ | |_  ___   _ __ ___    ___ | |_ | |__    ___    __| |
     | '_ \ | '__|| |\ \ / // _` || __|/ _ \ | '_ ` _ \  / _ \| __|| '_ \  / _ \  / _` |
     | |_) || |   | | \ V /| (_| || |_|  __/ | | | | | ||  __/| |_ | | | || (_) || (_| |
     | .__/ |_|   |_|  \_/  \__,_| \__|\___| |_| |_| |_| \___| \__||_| |_| \___/  \__,_|
     | |
     |_|
     ****************************************私有方法区*******************************************
     */

    private static void setTitles(SheetInfo sheetInfo, Class<?> clazz, ExportConfig config){
        List<Field> fields = ClassUtils.getAllFields(clazz);
        List<HeaderInfo> headerInfos = Lists.newArrayList();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Header.class)) {
                Header anno = m.getAnnotation(Header.class);
                HeaderInfo headerInfo = new HeaderInfo();
                headerInfo.setName(m.getName());
                headerInfo.setTitle(anno.name());
                headerInfo.setPrompt(anno.prompt());
                headerInfo.setRegex(anno.regex());
                headerInfo.setMode(anno.mode());
                headerInfo.setFormat(anno.format());
                headerInfo.setAlign(anno.align());
                headerInfo.setOrder(Integer.valueOf(anno.order()));
                headerInfo.setType(anno.type());
                headerInfo.setDropdown(anno.dropdown());
                if( null != config && !CollectionUtils.isEmpty(config.getDropDowns()) ){
                    List<?> dropdown = config.getDropDowns().get(anno.name());
                    if( !CollectionUtils.isEmpty(dropdown) ){
                        headerInfo.setDropdown(dropdown.toArray(new String[]{}));
                    }
                }
                sheetInfo.getHeaderInfos().put(anno.name(), headerInfo);
                headerInfos.add(headerInfo);
            }
        }
        if( CollectionUtils.isEmpty(sheetInfo.getHeaderInfos()) ){
            return;
        }
        Collections.sort(headerInfos, new Comparator<HeaderInfo>() {
            @Override
            public int compare(final HeaderInfo o1, final HeaderInfo o2) {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        });
        for (HeaderInfo headerInfo : headerInfos) {
            sheetInfo.getHeaderNames().add(headerInfo.getTitle());
        }
    }

    /**
     * 转移数据，从excel到list
     **/
    private static void fetchDataFromExcel(SheetInfo sheetInfo){
        Sheet sheet = sheetInfo.getSheet();
        boolean hasOneRight = false;
        TitleType titleType = sheetInfo.getTitleType();
        if(TitleType.NONE.equals(titleType) && !CollectionUtils.isEmpty(sheetInfo.getRows())){
            Integer firstNum = 0;
            Integer length = sheetInfo.getRows().size();
            for ( int i = firstNum; i<length; i++ ){
                Integer rowNum = sheetInfo.getRows().get(i);
                if (!fetchLineData(sheetInfo, sheet.getRow(rowNum))) { continue; }
                hasOneRight = true;
            }
        }else{
            Integer firstNum = getBeginRowNum(sheetInfo, sheet);
            Integer length = firstNum + sheetInfo.getMaxRowNum() + 10;
            for (int i = firstNum; i < length; i++) {
                if (!fetchLineData(sheetInfo, sheet.getRow(i))) { continue; }
                hasOneRight = true;
            }
        }
        /*至少有一行数据**/
        if (!hasOneRight) {
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"没有一行正确的数据,请核查文件内容!");
        }
        if (sheetInfo.getDatas().size() > sheetInfo.getMaxRowNum()) {
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"超出模板最大行数限制:" + sheetInfo.getMaxRowNum() + "行");
        }
    }

    /**
     * 从Excel获取每一行的数据
     **/
    private static boolean fetchLineData(SheetInfo sheetInfo, Row row) {
        if( null == row ){ return false; }
        if(TitleType.NONE.equals(sheetInfo.getTitleType())){
            return fetchLineDataWithNoneTitle(sheetInfo, row);
        }else if( null != sheetInfo.getClazz() ){
            return fetchDataLineWithClassTitle(sheetInfo, row);
        }else if( !CollectionUtils.isEmpty(sheetInfo.getHeaderInfos()) ){
            return fetchDataLineWithStringTitle(sheetInfo, row);
        }else{
            return false;
        }
    }

    /**字符串类型的表头**/
    private static boolean fetchDataLineWithStringTitle(SheetInfo sheetInfo, Row row) {
        //是否整行为空
        boolean isAllBlank = true;
        Map<String,Object> t = Maps.newHashMap();
        Row headerRow = sheetInfo.getHeaderRow();
        Map<String, HeaderInfo> headerInfos = sheetInfo.getHeaderInfos();
        for (int column = 0; column < headerRow.getLastCellNum(); column++) {
            Cell headerCell = headerRow.getCell(column);
            HeaderInfo headerInfo = headerInfos.get(headerCell.getStringCellValue());
            if( null == headerInfo ){
                continue;
            }
            if (StringUtils.isBlank(headerInfo.getName())) {
                continue;
            }
            Cell cell = row.getCell(column);
            Object cellValue = CellValueUtils.getCellValueByType(cell, headerInfo);
            if( null == cellValue ){
                continue;
            }
            t.put(headerInfo.getName(),cellValue);
            isAllBlank = false;
        }
        if( !isAllBlank ){
            sheetInfo.getDatas().add(t);
        }
        return !isAllBlank;
    }

    /**对象类型的表头**/
    private static boolean fetchDataLineWithClassTitle(SheetInfo sheetInfo, Row row) {
        Object t;
        try {
            t = sheetInfo.getClazz().newInstance();
        } catch (Exception e) {
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"创建数据对象失败,请确认是否传入对象");
        }
        if(ClassUtils.isExtendDataType(t)){
            throw new BaseRuntimeException(ErrorCode.VALID_ERROR.getCode(),"数据对象为非模板对象,请确认传入对象的正确性");
        }
        //是否整行为空
        boolean isAllBlank = true;
        Row headerRow = sheetInfo.getHeaderRow();
        Map<String, HeaderInfo> headerInfos = sheetInfo.getHeaderInfos();
        for (int column = 0; column < headerRow.getLastCellNum(); column++) {
            Cell headerCell = headerRow.getCell(column);
            HeaderInfo headerInfo = headerInfos.get(headerCell.getStringCellValue());
            if( null == headerInfo ){
                continue;
            }
            if (StringUtils.isBlank(headerInfo.getName())) {
                continue;
            }
            Cell cell = row.getCell(column);
            Object cellValue = CellValueUtils.getCellValueByType(cell, headerInfo);
            if( null == cellValue ){
                continue;
            }
            if( null == headerInfo.getType() || Objects.class.equals(headerInfo.getType())){
                headerInfo.setType(ClassUtils.getFieldType(t.getClass(),headerInfo.getName()));
            }
            if( ClassUtils.setValue(t,headerInfo.getName(),cellValue) ){
                isAllBlank = false;
            }
        }
        CellValidUtils.validExcelLineData(sheetInfo,row.getRowNum(),t);
        if( !isAllBlank ){
            sheetInfo.getDatas().add(t);
        }
        return !isAllBlank;
    }

    /**无表头**/
    private static boolean fetchLineDataWithNoneTitle(SheetInfo sheetInfo, Row row) {
        boolean isAllBlank = true;
        List t = Lists.newArrayList();
        for (Integer column : sheetInfo.getColumns()) {
            Cell cell = row.getCell(column);
            Object cellValue = CellValueUtils.getCellValueByType(cell, null);
            if( null == cellValue ){
                continue;
            }
            t.add(cellValue);
            isAllBlank = false;
        }
        if( !isAllBlank ){
            sheetInfo.getDatas().add(t);
        }
        return !isAllBlank;
    }

    /**获取数据开始行**/
    private static Integer getBeginRowNum(SheetInfo sheetInfo, Sheet sheet) {
        Row headerRow = sheetInfo.getHeaderRow();
        Integer beginRowNum;
        Integer firstNum = sheet.getFirstRowNum();
        if( null != headerRow ){
            beginRowNum = headerRow.getRowNum() + 1;
            if (beginRowNum > firstNum) {
                firstNum = beginRowNum;
            }
        }
        return firstNum;
    }

    /**设置表头**/
    private static void writeExcelTitleDatas(SheetInfo sheetInfo, CellStyle headerStyle) {

        if( sheetInfo.isUseTemplate()){ return; }

        Row row;
        Integer rowNumber = sheetInfo.getBeginRow();
        Integer maxRowNum = sheetInfo.getMaxRowNum();
        Sheet sheet = sheetInfo.getSheet();
        row = sheet.createRow(rowNumber % maxRowNum);
        row.setHeight(sheetInfo.getRowHeight().shortValue());
        List<String> headerNames = sheetInfo.getHeaderNames();
        for (int j = 0; j < headerNames.size(); j++) {
            Cell headCell = row.createCell(j);
            String headerName = headerNames.get(j);
            DataValidation dropDownValidation = dropDownValidation(sheetInfo,headerName,row.getRowNum(),maxRowNum,j);
            if( null != dropDownValidation ){
                sheet.addValidationData(dropDownValidation);
            }
            DataValidation promptValidation = promptValidation(sheetInfo,headerName,row.getRowNum(),j);
            if( null != promptValidation ){
                sheet.addValidationData(promptValidation);
            }
            headCell.setCellStyle(headerStyle);
            gatherColumnsWidth(sheetInfo.getColumnWidths(), headerName, headerName, j);
            CellValueUtils.setCellValueByType(headCell, headerNames.get(j));
        }
        sheetInfo.setBeginRow(sheetInfo.getBeginRow()+1);
    }

    /**添加单元格提示**/
    private static DataValidation dropDownValidation(SheetInfo sheetInfo, String headerName, Integer row, Integer maxRow, Integer column) {

        Map<String, HeaderInfo> headerInfos = sheetInfo.getHeaderInfos();
        HeaderInfo headerInfo = headerInfos.get(headerName);

        String[] dropdown = headerInfo.getDropdown();

        if( null == dropdown || dropdown.length == 0 ){ return null; }

        DataValidationHelper helper = sheetInfo.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(dropdown);

        CellRangeAddressList regions = new CellRangeAddressList(row+1,maxRow,column,column);
        DataValidation dataValidation = configDataValidationStatus(sheetInfo, helper, constraint, regions);

        dataValidation.setEmptyCellAllowed(true);

        return dataValidation;
    }

    /**添加单元格提示**/
    private static DataValidation promptValidation(SheetInfo sheetInfo, String headerName, Integer row, Integer column) {

        Map<String, HeaderInfo> headerInfos = sheetInfo.getHeaderInfos();
        HeaderInfo headerInfo = headerInfos.get(headerName);

        String prompt = headerInfo.getPrompt();

        if( StringUtils.isBlank(prompt) ){ return null; }

        DataValidationHelper helper = sheetInfo.getSheet().getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("BB1");

        CellRangeAddressList regions = new CellRangeAddressList(row,row,column,column);
        DataValidation dataValidation = configDataValidationStatus(sheetInfo, helper, constraint, regions);

        dataValidation.createPromptBox("提示", prompt);
        dataValidation.setEmptyCellAllowed(true);
        dataValidation.setShowPromptBox(true);

        return dataValidation;
    }

    private static DataValidation configDataValidationStatus(SheetInfo sheetInfo, DataValidationHelper helper, DataValidationConstraint constraint, CellRangeAddressList regions) {
        DataValidation dataValidation = helper.createValidation(constraint, regions);

        //2003
        if (Version.XLS.equals(sheetInfo.getVersion())) {
            dataValidation.setSuppressDropDownArrow(false);
        } else if (Version.XLSX.equals(sheetInfo.getVersion())) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        return dataValidation;
    }

    /**
     * 收集列宽数据
     *
     * @param columnWidths 列宽数据列表
     * @param headerName   表头名称
     * @param content      单元格内容
     * @param j            列编号
     */
    private static void gatherColumnsWidth(Map<Integer, Integer> columnWidths, String headerName, String content, int j) {
        String[] splits = content.split("");

        int length = countCellLength(splits);

        length = (int) (length * 1.5);

        if (headerName.equalsIgnoreCase(content)) {
            if (headerName.contains(ValidRules.CHINA_LEFT_BRACKET)) {
                headerName = headerName.replace(ValidRules.CHINA_LEFT_BRACKET, ValidRules.NEW_LINE_SIGN + ValidRules.CHINA_LEFT_BRACKET);
                String lengthStr = headerName.substring(0, headerName.indexOf(ValidRules.CHINA_LEFT_BRACKET));
                length = lengthStr.getBytes().length;
            }
            columnWidths.put(j, length);
        } else {
            int oldLength = null == columnWidths.get(j) ? 0 : columnWidths.get(j);
            if (length > oldLength) {
                oldLength = length;
            }
            if (oldLength < ValidRules.DEFAULT_CELL_WIDTH) {
                oldLength = ValidRules.DEFAULT_CELL_WIDTH;
            }
            columnWidths.put(j, oldLength);
        }
    }

    /**单元格内容长度计算**/
    private static int countCellLength(String[] splits) {
        int length = 0;
        for (String split : splits) {
            if (StringUtils.isBlank(split)) {
                continue;
            }
            length++;
            int byteLength = split.getBytes().length;
            //中文,UTF8编码,字节长度为3
            if (3 == byteLength) {
                length++;
            }
        }
        return length;
    }


    /**写excel行数据**/
    private static void writeExcelRowDatas(SheetInfo sheetInfo) {
        List<String> headerNames = sheetInfo.getHeaderNames();
        List<CellStyle> oddStyle = sheetInfo.getOddStyle();
        List<CellStyle> evenStyle = sheetInfo.getEvenStyle();
        Map<String, HeaderInfo> headerInfos = sheetInfo.getHeaderInfos();
        Map<Integer,Integer> columnWidths = sheetInfo.getColumnWidths();
        Integer unionWith = sheetInfo.getUnionWith();
        List list = sheetInfo.getDatas();

        for (int i = 0,length = sheetInfo.getDatas().size();i<length;i++) {
            Row row = sheetInfo.getSheet().createRow(sheetInfo.getBeginRow()+i);
            row.setHeight(sheetInfo.getRowHeight().shortValue());
            for (int j = 0; j < headerNames.size(); j++) {
                CellStyle cellStyle = getCellStyle(oddStyle, evenStyle, i, j);
                setExcelCellData(row, list, headerInfos, headerNames, cellStyle, columnWidths, i, j);
            }
        }
        // 如果使用模板,则不修改模板本身列的宽度
        SheetUtils.setColumnsWidth(sheetInfo.getSheet(), columnWidths, unionWith);
    }

    /**
     * 根据行列好获取单元格样式
     **/
    private static CellStyle getCellStyle(List<CellStyle> columnOddStyle, List<CellStyle> columnEvenStyle, int i, int j) {
        CellStyle cellStyle;
        if (i % 2 == 0) {
            cellStyle = columnOddStyle.get(j);
        } else {
            cellStyle = columnEvenStyle.get(j);
        }
        return cellStyle;
    }

    /**
     * 设置excel单元格数据
     **/
    private static void setExcelCellData(Row row, List list, Map<String, HeaderInfo> headerInfos, List<String> headerNames, CellStyle cellStyle, Map<Integer, Integer> columnWidths, int i, int j) {
        Cell cell = row.createCell(j);
        cell.setCellStyle(cellStyle);

        String headerName = headerNames.get(j);
        HeaderInfo headerInfo = headerInfos.get(headerName);

        Object obj = list.get(i);
        Object value;
        try {
            if( obj instanceof Map ){
                value = ((Map)obj).get(headerInfo.getName());
            }else{
                value = PropertyUtils.getSimpleProperty(obj, headerInfo.getName());
            }
        } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
            throw new BaseRuntimeException(e);
        }

        CellValueUtils.setCellValueByType(cell, headerInfo, value);
        gatherColumnsWidth(columnWidths, headerName, String.valueOf(value), j);
    }

    /**转换字符串为表头**/
    private static void transferTitles(Map<String, String> headerTitles, Map<String, Class> headerClasses, List<String> headerNames, String titles) {
        List<String> list = StringUtils.splitToList(titles,",");
        if( !CollectionUtils.isEmpty(list) ){
            for (String title : list) {
                List<String> values = StringUtils.splitToList(title,":");
                if(!CollectionUtils.isEmpty(values) && values.size() == 2){
                    String key = values.get(0);
                    String value = values.get(1);
                    headerTitles.put(key,value);
                    headerNames.add(key);
                }
                if(!CollectionUtils.isEmpty(values) && values.size() == 3){
                    String key = values.get(0);
                    String value = values.get(1);
                    headerTitles.put(key,value);
                    headerNames.add(key);

                    String typeStr = values.get(2);
                    Class<?> type = ClassUtils.getBaseClass(typeStr);
                    if( null != type ){
                        headerClasses.put(key,type);
                    }
                }
            }
        }
    }

    /**读取某一行**/
    private static boolean readHeaderRow(List<String> titleNames, Row row, List<String> titles) {
        //根据表头的列数来获取表头,excel表头可以比真实表头多10列
        int cellNum = row.getLastCellNum();
        boolean isAllTitleRight = true;
        for (int i = 0; i < cellNum; i++) {
            Cell cell = row.getCell(i);
            if( null == cell ){ continue; }
            String titleName = cell.toString();
            if( StringUtils.isBlank(titleName) ){
                isAllTitleRight = false;
                continue;
            }
            if( !titleNames.contains(titleName) ){
                isAllTitleRight = false;
                continue;
            }
            titles.add(titleName);
        }
        return isAllTitleRight;
    }
}
