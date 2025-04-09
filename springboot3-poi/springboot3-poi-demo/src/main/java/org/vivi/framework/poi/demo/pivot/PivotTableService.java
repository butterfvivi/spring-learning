package org.vivi.framework.poi.demo.pivot;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class PivotTableService {

    /**
     * @param clazz
     * @return
     */
    public static Map<String, List<String>> getAliasNameFieldNameAndTitle(Class<?> clazz) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> headerList = new ArrayList<String>();
        List<String> fieldList = new ArrayList<String>();
        List<String> titleList = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldsByAnnotation = Arrays.stream(fields).filter(field -> field.getAnnotation(PivotTableFiled.class) != null)
                .collect(Collectors.toList());
        if (fieldsByAnnotation.isEmpty()) {
            return map;
        }
        for (Field field : fieldsByAnnotation) {
            PivotTableFiled pivotTableFiled = field.getAnnotation(PivotTableFiled.class);
            if (pivotTableFiled != null) {
                headerList.add(pivotTableFiled.header());
                fieldList.add(field.getName());
            }
        }
        PivotTableTitle title = clazz.getAnnotation(PivotTableTitle.class);
        if (title != null) {
            titleList.add(title.title());
        } else {
            titleList.add("透视表");
        }
        //map.put(HEADER, headerList);
        //map.put(FIELD, fieldList);
        //map.put(TITLE, titleList);
        return map;
    }

//    /**
//     * 获取字段对应的label位置，该字段应该在列，行还是值上
//     */
//    public static List<PivotTableLabel> getLabel(Class<?> clazz) {
//        Field[] fields = clazz.getDeclaredFields();
//        List<PivotTableLabel> labels = new ArrayList<PivotTableLabel>();
//        List<Field> fieldsByAnnotation = Arrays.stream(fields).filter(field -> field.getAnnotation(PivotTableFiled.class) != null)
//                .collect(Collectors.toList());
//        for (int i = 0; i < fieldsByAnnotation.size(); i++) {
//            PivotTableFiled annotation = fieldsByAnnotation.get(i).getAnnotation(PivotTableFiled.class);
//            if (annotation != null) {
//                PivotTableLabel label = new PivotTableLabel();
//                label.setIndex(i);
//                label.setArea(annotation.area());
//                label.setSummerType(annotation.summaryType());
//                label.setAliasName(annotation.aliasName());
//                labels.add(label);
//            }
//        }
//        return labels;
//    }
}
