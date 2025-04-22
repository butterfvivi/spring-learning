package org.vivi.framework.ireport.demo.report.resource;

import org.vivi.framework.ireport.demo.common.exception.ExcelException;
import org.vivi.framework.ireport.demo.report.style.ExcelCellStyle;

public class ExcelRenderResourceFactory {

//    public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb,
//                                                            DataFormatDecider dataFormatDecider) {
//        PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap(dataFormatDecider);
//        Map<String, String> headerNamesMap = new LinkedHashMap<>();
//        List<String> fieldNames = new ArrayList<>();
//
//        for (Field field : getAllFields(type)) {
//            if (field.isAnnotationPresent(ExcelColumn.class)) {
//                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
//                styleMap.put(
//                        String.class,
//                        ExcelCellKey.of(field.getName(), ExcelRenderLocation.HEADER),
//                        getCellStyle(decideAppliedStyleAnnotation(classDefinedHeaderStyle, annotation.headerStyle())), wb);
//                Class<?> fieldType = field.getType();
//                styleMap.put(
//                        fieldType,
//                        ExcelCellKey.of(field.getName(), ExcelRenderLocation.BODY),
//                        getCellStyle(decideAppliedStyleAnnotation(classDefinedBodyStyle, annotation.bodyStyle())), wb);
//                fieldNames.add(field.getName());
//                headerNamesMap.put(field.getName(), annotation.headerName());
//            }
//        }
//
//        if (styleMap.isEmpty()) {
//            throw new ExcelException(String.format("Class %s has not @ExcelColumn at all", type));
//        }
//        return new ExcelRenderResource(styleMap, headerNamesMap, fieldNames);
//    }
//
//    private static ExcelCellStyle getCellStyle(ExcelColumnStyle excelColumnStyle) {
//        Class<? extends ExcelCellStyle> excelCellStyleClass = excelColumnStyle.excelCellStyleClass();
//        // 1. Case of Enum
//        if (excelCellStyleClass.isEnum()) {
//            String enumName = excelColumnStyle.enumName();
//            return findExcelCellStyle(excelCellStyleClass, enumName);
//        }
//
//        // 2. Case of Class
//        try {
//            return excelCellStyleClass.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new ExcelException(e.getMessage(), e);
//        }
//    }


    @SuppressWarnings("unchecked")
    private static ExcelCellStyle findExcelCellStyle(Class<?> excelCellStyles, String enumName) {
        try {
            return (ExcelCellStyle) Enum.valueOf((Class<Enum>) excelCellStyles, enumName);
        } catch (NullPointerException e) {
            throw new ExcelException("enumName must not be null", e);
        } catch (IllegalArgumentException e) {
            throw new ExcelException(
                    String.format("Enum %s does not name %s", excelCellStyles.getName(), enumName), e);
        }
    }
}
