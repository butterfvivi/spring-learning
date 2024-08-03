package org.vivi.framework.iexcelbatch.listener;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.vivi.framework.iexcelbatch.entity.dto.CellSelectDto;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomSheetWriteHandler implements SheetWriteHandler {

    private CellSelectDto cellSelectDataVo;

    private List<String> selectDataList;

    /**
     * 开启隐藏页设置下拉值([1,50]) 使用隐藏列的形式，设置下拉数据多的情况会出现导出下拉值不回显bug
     */
    private static final Integer ON_SHEET_LIMIT = 1;


    public CustomSheetWriteHandler(CellSelectDto cellSelectDataVo, List<String> selectDataList) {
        this.cellSelectDataVo = cellSelectDataVo;
        this.selectDataList = selectDataList;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (CollectionUtils.isNotEmpty(selectDataList) && selectDataList.size() <= ON_SHEET_LIMIT) {
            writerSelectData(writeSheetHolder);
            return;
        }
        writeLongSheetHolder(writeWorkbookHolder, writeSheetHolder);
    }

    /**
     * 不超过的直接设置下拉值  有bug 请使用隐藏列的方式
     *
     * @param writeSheetHolder
     */
    private void writerSelectData(WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        // 设置下拉列表的行： 首行，末行，首列，末列
        CellRangeAddressList regions = new CellRangeAddressList(cellSelectDataVo.getFirstRow(),
                cellSelectDataVo.getLastRow(),
                cellSelectDataVo.getFirstCol(),
                Objects.isNull(cellSelectDataVo.getLastCol()) ? cellSelectDataVo.getFirstCol() : cellSelectDataVo.getLastCol()
        );
        // 直接设置下拉选
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(this.selectDataList.toArray(new String[0]));
        // 设置约束
        DataValidation validation = dataValidationHelper.createValidation(constraint, regions);
        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        //validation.setEmptyCellAllowed(true); // 设置允许空白单元格
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("文本不规范", "请从下拉框中选择!");
        sheet.addValidationData(validation);
    }

    /**
     * 超过255 的设置隐藏的sheet处理下拉
     *
     * @param writeWorkbookHolder
     * @param writeSheetHolder
     */
    private void writeLongSheetHolder(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 设置下拉列表的行： 首行，末行，首列，末列
        String[] classLevels = this.selectDataList.toArray(new String[this.selectDataList.size()]);
        CellRangeAddressList regions = new CellRangeAddressList(cellSelectDataVo.getFirstRow(),
                cellSelectDataVo.getLastRow(),
                cellSelectDataVo.getFirstCol(),
                Objects.isNull(cellSelectDataVo.getLastCol()) ? cellSelectDataVo.getFirstCol() : cellSelectDataVo.getLastCol()
        );
        Workbook workBook = writeWorkbookHolder.getWorkbook();
        // 创建一个隐藏的sheet，存放下拉框选项
        Sheet hiddenSheet = workBook.createSheet("hiddenSelect");
        //  把下拉框列表数据放进隐藏sheet
        Cell cell = null;
        for (int i = 0; i < classLevels.length; i++) {
            Row row = hiddenSheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(classLevels[i]);
        }
        Name nameCell = workBook.createName();
        nameCell.setNameName(hiddenSheet.getSheetName());
        nameCell.setRefersToFormula(hiddenSheet.getSheetName() + "!$A$1:$A$" + classLevels.length);
        //隐藏sheet
        workBook.setSheetHidden(workBook.getSheetIndex(hiddenSheet), true);
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint createExplicitListConstraint = dataValidationHelper.createFormulaListConstraint(hiddenSheet.getSheetName());
        // 设置约束
        DataValidation validation = dataValidationHelper.createValidation(createExplicitListConstraint, regions);
        // 阻止输入非下拉选项的值
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        //validation.setEmptyCellAllowed(true); // 设置允许空白单元格
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("文本不规范", "请从下拉框中选择!");
        sheet.addValidationData(validation);
    }
}
