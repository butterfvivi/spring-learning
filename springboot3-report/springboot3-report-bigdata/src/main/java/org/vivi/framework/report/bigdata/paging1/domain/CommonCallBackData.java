package org.vivi.framework.report.bigdata.paging1.domain;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.vivi.framework.report.bigdata.paging1.annionate.ExcelStyle;

import java.lang.reflect.Field;
import java.util.List;

@Data
public class CommonCallBackData<T> {
    private Font font;
    private CellStyle cellStyle;
    private List<T> dataList;
    private Integer relativeRowIndex;
    private Cell cell;
    private Workbook workbook;
    private Field field;
    private ExcelStyle annotation;
    private Boolean isHead;
    private Boolean isRequire;
}


