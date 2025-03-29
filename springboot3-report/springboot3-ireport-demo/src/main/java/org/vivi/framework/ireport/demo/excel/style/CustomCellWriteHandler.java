package org.vivi.framework.ireport.demo.excel.style;


import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class CustomCellWriteHandler implements CellWriteHandler {
    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();

        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        //获取表头总共有多少列
        short lastCellNum = writeSheetHolder.getSheet().getRow(0).getLastCellNum();
        //过滤表头
        if (!aBoolean) {
            //获取当前行数
            int rowNum = integer + 1;
            //每一个单元格都会执行afterCellDispose此方法,每执行一次单元格数据+1,此处是校验当前行的全部单元格加载完毕
            if (writeSheetHolder.getSheet().getRow(rowNum).getLastCellNum() == lastCellNum) {
                //加载完毕执行业务代码(可按照自己实际业务进行修改)
                for (int i = 0; i < lastCellNum; i++) {
                    if (i % 2 != 0) {
                        //指定单元格修改样式
                        writeSheetHolder.getSheet().getRow(rowNum).getCell(i).setCellStyle(cellStyle);
                    }
                    //录入的数据全部是String类型,直接获取就行,如果有不同类型数据,需要添加类型判断
                    String stringCellValue = writeSheetHolder.getSheet().getRow(rowNum).getCell(i).getStringCellValue();
                    //setCellValue会根据数据自动修改单元格格式
                    if (StringUtils.isEmpty(stringCellValue)) {
                        writeSheetHolder.getSheet().getRow(rowNum).getCell(i).setCellValue("");
                    } else {
                        writeSheetHolder.getSheet().getRow(rowNum).getCell(i).setCellValue(Double.parseDouble(stringCellValue));
                    }
                }
            }
        }
    }
}

