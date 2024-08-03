package org.vivi.framework.iexcelbatch.listener;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.vivi.framework.iexcelbatch.entity.dto.CellSelectDataPair;
import org.vivi.framework.iexcelbatch.entity.dto.CellSelectDto;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomBatchSheetWriteHandler implements SheetWriteHandler {

    private List<CellSelectDataPair> dataPairs;

    /**
     * 开启隐藏页设置下拉值([1,50]) 使用隐藏列的形式，设置下拉数据多的情况会出现导出下拉值不回显bug
     */
    private static final Integer ON_SHEET_LIMIT = 1;


    public CustomBatchSheetWriteHandler(List<CellSelectDataPair> dataPairs) {
        this.dataPairs = dataPairs;
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        for (int i = 0; i < dataPairs.size(); i++) {
            CellSelectDataPair dataPair = dataPairs.get(i);
            CellSelectDto cellSelectDataVo = dataPair.getCellSelectDataVo();
            List<String> selectDataList = dataPair.getSelectDataList();
            if (CollectionUtils.isNotEmpty(selectDataList) && selectDataList.size() <= ON_SHEET_LIMIT) {
                writerSelectData(writeSheetHolder, cellSelectDataVo, selectDataList);
            } else {
                writeLongSheetHolder(writeSheetHolder, writeWorkbookHolder.getWorkbook(), cellSelectDataVo, selectDataList, i);
            }
        }
    }

    /**
     * 不超过的直接设置下拉值
     *
     * @param writeSheetHolder
     */
    private void writerSelectData(WriteSheetHolder writeSheetHolder, CellSelectDto cellSelectDataVo,
                                  List<String> selectDataList) {
        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        // 设置下拉列表的行： 首行，末行，首列，末列
        CellRangeAddressList regions = new CellRangeAddressList(cellSelectDataVo.getFirstRow(),
                cellSelectDataVo.getLastRow(),
                cellSelectDataVo.getFirstCol(),
                Objects.isNull(cellSelectDataVo.getLastCol()) ? cellSelectDataVo.getFirstCol() : cellSelectDataVo.getLastCol());
        // 直接设置下拉选
        DataValidationConstraint constraint = dataValidationHelper.createExplicitListConstraint(selectDataList.toArray(new String[selectDataList.size()]));
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
     * @param writeSheetHolder
     * @param workBook
     */
    private void writeLongSheetHolder(WriteSheetHolder writeSheetHolder, Workbook workBook,
                                      CellSelectDto cellSelectDataVo, List<String> selectDataList,
                                      int i) {

        // 创建一个隐藏的sheet，存放下拉框选项
        Sheet hiddenSheet = workBook.createSheet("hiddenSelect" + i);

        // 把下拉框列表数据放进隐藏sheet
        for (int j = 0; j < selectDataList.size(); j++) {
            Row row = hiddenSheet.createRow(j);
            Cell cell = row.createCell(0);
            cell.setCellValue(selectDataList.get(j));
        }

        Name nameCell = workBook.createName();
        nameCell.setNameName(hiddenSheet.getSheetName());
        nameCell.setRefersToFormula(hiddenSheet.getSheetName() + "!$A$1:$A$" + selectDataList.size());

        // 设置下拉框范围
        CellRangeAddressList regions = new CellRangeAddressList(
                cellSelectDataVo.getFirstRow(),
                cellSelectDataVo.getLastRow(),
                cellSelectDataVo.getFirstCol(),
                Objects.isNull(cellSelectDataVo.getLastCol()) ? cellSelectDataVo.getFirstCol() : cellSelectDataVo.getLastCol()
        );

        Sheet sheet = writeSheetHolder.getSheet();
        DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
        DataValidationConstraint createExplicitListConstraint = dataValidationHelper.createFormulaListConstraint(hiddenSheet.getSheetName());
        //隐藏创建的sheet
        workBook.setSheetHidden(workBook.getSheetIndex(hiddenSheet), true);

        // 设置约束
        DataValidation validation = dataValidationHelper.createValidation(createExplicitListConstraint, regions);
        validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        validation.setShowErrorBox(true);
        validation.setSuppressDropDownArrow(true);
        validation.createErrorBox("文本不规范", "请从下拉框中选择!");
        sheet.addValidationData(validation);
    }
}
