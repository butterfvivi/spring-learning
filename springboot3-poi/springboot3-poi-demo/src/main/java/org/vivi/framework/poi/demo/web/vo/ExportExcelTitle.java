package org.vivi.framework.poi.demo.web.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vivi.framework.poi.demo.model.ExportExcelBean;
import org.vivi.framework.poi.demo.model.ExportExcelTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangFZ
 * @date 2020/5/8 15:24
 **/
public class ExportExcelTitle {

    @Getter
    @AllArgsConstructor
    public enum table1 {

        INDEX("序号", "index"),
        CODE("标题单号", "code"),
        NAME("标题名称",  "name"),
        NAME1("标题名称",  "name1");

        /**
         * 中文标题
         */
        private String titleCn;

        /**
         * 查出的值对应Map的key
         */
        private String valueKey;

        /**
         * 获取excel对应的数据
         */
        public static ExportExcelBean getValue() {
            List<String> titleCn = new ArrayList<>();
            List<String> valueKey = new ArrayList<>();
            for (table1 excel : table1.values()) {
                titleCn.add(excel.titleCn);
                valueKey.add(excel.valueKey);
            }
            ExportExcelBean excelBean = new ExportExcelBean();
            excelBean.setHeaders(titleCn);
            excelBean.setTitle("测试导出信息1");
            excelBean.setSheetName("测试sheet");
            excelBean.setKeys(valueKey);
            excelBean.setTheme(ExportExcelTheme.BLUE);
            excelBean.setVerticalMergerColumnHeaders(titleCn);
            excelBean.setHorizontalMergerColumnHeaders(titleCn);
            return excelBean;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum table2 {

        INDEX("序号", "index"),
        CODE("标题名称", "code"),
        NAME("标题名称",  "name"),
        BRAND("数据品牌", "brand");

        /**
         * 中文标题
         */
        private String titleCn;

        /**
         * 查出的值对应Map的key
         */
        private String valueKey;

        /**
         * 获取excel对应的数据
         */
        public static ExportExcelBean getValue() {
            List<String> titleCn = new ArrayList<>();
            List<String> valueKey = new ArrayList<>();
            for (table2 excel : table2.values()) {
                titleCn.add(excel.titleCn);
                valueKey.add(excel.valueKey);
            }
            ExportExcelBean excelBean = new ExportExcelBean();
            excelBean.setHeaders(titleCn);
            excelBean.setTitle("测试导出信息2");
            excelBean.setSheetName("测试sheet2");
            excelBean.setKeys(valueKey);
            excelBean.setTheme(ExportExcelTheme.ORANGE);
            excelBean.setVerticalMergerColumnHeaders(titleCn);
            // excelBean.setHorizontalMergerColumnHeaders(titleCn);
            return excelBean;
        }
    }
}