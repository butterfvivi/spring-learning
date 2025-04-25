package org.vivi.framework.ireport.demo.report.style;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public class ExcelCellStyleStrategy extends AbstractCellStyleStrategy {

    /**
     * 单元格格式列表（格式：GENERAL、CURRENCY_￥、CURRENCY_$、DATE、NUMERIC）
     */
    private final List<String> cellDataTypes;

    /**
     * WorkBoot
     */
    private Workbook workbook;

    /**
     * 构造方法，创建对象时传入需要定制的表头信息队列
     */
    public ExcelCellStyleStrategy(List<String> cellDataTypes) {
        this.cellDataTypes = cellDataTypes;
    }

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        // 处理表头的
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {

        CellStyle cellStyle = workbook.createCellStyle();

        String cellValue = cell.getStringCellValue();
        String dataTypes = cellDataTypes.get(head.getColumnIndex());

        switch (dataTypes) {
            case "DATE":
                // 时间
                if (StrUtil.isNotBlank(cellValue)) {
                    DateTime dateTime = DateUtil.parseDateTime(cellValue);
                    cell.setCellValue(DateUtil.format(dateTime, "yyyy/m/d h:mm"));
                    cellStyle.setDataFormat((short) 164);
                }

                break;

            case "NUMERIC":
                // 数字
                cell.setCellType(CellType.NUMERIC);
                // cellStyle.setDataFormat((short) 1);

                // 自定义格式
                // 1 -> "0",        表示整数
                // 2 -> "0.00",     表示浮点数
                // 3 -> "#,##0",    表示三个数字加一个","格式的整数
                // 4 -> "#,##0.00", 表示三个数字加一个","格式的浮点数

                if (StrUtil.isBlank(cellValue)) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(Integer.parseInt(cellValue));
                }

                break;

            default:
                if (dataTypes.startsWith("CURRENCY_")) {
                    // 货币
                    String currency = dataTypes.substring(9);
                    cell.setCellValue(StrUtil.format("{}{}", currency, StrUtil.isBlank(cellValue) ? "-" : cellValue));
                    cellStyle.setDataFormat((short) 42);
                }

                // 默认是通用类型，无需额外处理
        }

        cell.setCellStyle(cellStyle);

    }

}
