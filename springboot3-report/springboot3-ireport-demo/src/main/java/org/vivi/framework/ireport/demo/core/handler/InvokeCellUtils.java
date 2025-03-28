package org.vivi.framework.ireport.demo.core.handler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * handle cell and sheet utils
 */
public class InvokeCellUtils {

    /**
     * 获取当前单元格的是上一个单元格对象
     */
    public static Cell getUpCell(Cell cell) {
        int curRowIndex = cell.getRowIndex();
        //如果没有上一个单元格，返回null
        if (curRowIndex == 0) return null;
        return cell.getSheet().getRow(curRowIndex - 1).getCell(cell.getColumnIndex());
    }

    /**
     * 获取当前单元格左边的Cell
     **/
    public static Cell getLeftCellVal(Cell cell) {
        int curColIndex = cell.getColumnIndex();
        //如果没有左边值，返回null
        if (curColIndex == 0) return null;
        return cell.getSheet().getRow(cell.getRowIndex()).getCell(curColIndex - 1);
    }

    /**
     *
     * 获取不同类型单元格的值
     * **/
    public static Object getCellValue(Cell cell) {
        Object val = "";
        CellType cellType = cell.getCellType();
        // 处理布尔类型的值
        if (cellType == CellType.BOOLEAN) {
            val = cell.getBooleanCellValue();
        }
        // 处理数字类型的值
        else if (cellType == CellType.NUMERIC) {
            val = cell.getNumericCellValue();
        }
        //处理字符串类型的值
        else if (cellType == CellType.STRING) {
            val = cell.getStringCellValue();
        }
        return val;
    }
}
