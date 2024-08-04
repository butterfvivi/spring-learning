package org.vivi.framework.iexceltoolkit.toolkit.invoke;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

/**
 * excel每一个sheet页面都是一个sheet对象。sheet对象由n个cell对象组成，每个cell都可以设置单独的属性。
 * 在excel中。可以理解成一个xy坐标系，给定一个坐标能确定唯一一个cell
 */
@Data
public class HandleCell implements CellWriteHandler {

    //需要合并的列
    private Set<Integer> mergerColIndex;
    //合并列的行界限,从第N行开始合并列,如果值为空，则直接从非表头的地方开始合并
    private Integer mergerRowIndexLimit;
    //合并列界限，默认以mergerColIndex数值升序第一个为界限
    private Integer mergerColIndexLimit;
    //表头最大显示多少字。后面所有列都会和他一样长,如果值为-1，就进行头自动合并
    private Integer headerWord;
    //列合并排除哪几行,主要是为了模板导出，头参数合并
    private Set<Integer> excludeRowIndex;
    //列合并是否排除最后一行，主要是为了模板导出，尾部参与合并
    private Boolean excludeTillRow;
    //记录坐标,k1=列位置，val=当前已合并单元格的上界
    private Map<Integer, Integer> coordinateMap = new HashMap<>();

    // 合并时是否依赖左侧要要依赖的行
    private Boolean isNeedLeftConditionMerge;

    //private List<Integer> mergerColIndexLimitList;

    public HandleCell(Set<Integer> mergerColIndex, Set<Integer> excludeRowIndex, Boolean excludeTillRow, Integer headerWord, Integer mergerRowIndexLimit, Integer mergerColIndexLimit, Boolean isNeedLeftConditionMerge) {
        this.mergerColIndex = mergerColIndex == null ? new HashSet<>() : mergerColIndex;
        this.excludeRowIndex = excludeRowIndex == null ? new HashSet<>() : excludeRowIndex;
        this.excludeTillRow = excludeTillRow == null ? false : excludeTillRow;
        this.isNeedLeftConditionMerge = isNeedLeftConditionMerge==null?true:isNeedLeftConditionMerge;
        if (headerWord != null && headerWord > 0) {
            this.headerWord = headerWord;
        } else {
            this.headerWord = -1;
        }
        //如果和并列，默认以第0列合并列
        if (mergerColIndex.size() != 0) {
            List<Integer> list = new ArrayList<>(mergerColIndex);
            list.sort(Comparator.comparingInt(o -> o));
            this.mergerColIndexLimit = mergerColIndexLimit == null ? list.get(0) : mergerColIndexLimit;
        }
        this.mergerRowIndexLimit = mergerRowIndexLimit;
    }

    /**
     * 合并单元格：需要注意的是，已经合并的单元格，必须先移除后，才可以合并。
     * 比如A2、A3已经合并了，这时候A3、A4在想来合并，就是报错。先移除A2、A3的合并区域，然后在从A2--->A4进行合并
     *
     * @param writeSheetHolder 当前写入的Sheet的持有者
     * @param writeTableHolder 当前写入的Table的持有者
     * @param list             写入单元格的数据列表
     * @param cell             当前写入的单元格
     * @param head             当前写入的表头
     * @param integer          当前写入的行号
     * @param isHead           当前行是否为表头
     */
    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean isHead) {
        //当前行
        int curRowIndex = cell.getRowIndex();
        //当前列
        int curColIndex = cell.getColumnIndex();
        //如果指定从第几行开始合并，则检验下
        if (isHead){
            if (headerWord != -1) {
                writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), 560 * headerWord);
            }
        }
        if (mergerRowIndexLimit != null && curRowIndex < mergerRowIndexLimit)  return;
        //合并B6、C6、B7、C7，那么对应的下标new CellRangeAddress(5, 6, 1, 2);，下表从0开始
        if (!isHead) {
            if (excludeRowIndex.contains(curRowIndex)) return;
            //判断是否需要排除最后一行
            if (excludeTillRow && cell.getSheet().getLastRowNum() == curRowIndex) return;
            //判断是否包含合并列
            if (mergerColIndex.size() != 0 && mergerColIndex.contains(curColIndex)) {
                //当前单元格值
                Object curCellValue = ExcelInvokeUtils.getCellValue(cell);
                //同列的上一个单元格对象
                Cell upCell = ExcelInvokeUtils.getUpCell(cell);
                Object preCellVale = null;
                if (upCell != null) {
                    preCellVale = ExcelInvokeUtils.getCellValue(upCell);
                }
                //如果当前单元格=上一行的单元格数据 && 当前行不等于合并的上限行时候，重新计算对应列的上限
                if ((curCellValue == preCellVale || curCellValue.equals(preCellVale)) && mergerRowIndexLimit !=curRowIndex) {
                    //默认当前列没有被合并
                    boolean isMerge = false;
                    Sheet sheet = cell.getSheet();
                    //获取当前sheet所有合并的区域.判断同列中，上一个单元格是否被合并了,如果合并了，就取消合并区域。重新合并到当前行结束
                    List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
                    //如果当前需要合并的列所在的行正好是此时合并列的界限。跳过
                    for (int i = 0; i < mergedRegions.size(); i++) {
                        CellRangeAddress merge = mergedRegions.get(i);
                        //如果上一行已经合并
                        if (merge.isInRange(upCell)) {
                            //移除合并的区域
                            sheet.removeMergedRegion(i);
                            //判断当前待合并单元格的开始行是从哪里开始
                            if (coordinateMap.size() != 0) {
                                Integer rowLimit = coordinateMap.get(curColIndex);
                                if (rowLimit != null) {
                                    //合并单元格最小上限不可以小于当前最低的界限:rowLimit <= mergerRowIndexLimit ? mergerRowIndexLimit : rowLimi
                                    merge.setFirstRow(rowLimit);
                                }
                            }
                            merge.setLastRow(curRowIndex);
                            sheet.addMergedRegion(merge);
                            isMerge = true;
                        }
                    }
                    //如果列未合并，则新建一个合并区域
                    if (!isMerge) {
                        //合并单元格，参数(1,2,3,4)表示合并第一行、第二行的第三第四列构建成的区域
                        CellRangeAddress cellAddresses = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
                        sheet.addMergedRegion(cellAddresses);
                    }
                }
                //如果上一个单元格与下一个单元格值不一致
                else {
                    if (this.isNeedLeftConditionMerge) {
                        //如果当前列恰好是所需合并列的界限，记录下标
                        if (mergerColIndex.contains(curColIndex)) {
                            mergerRowIndexLimit = curRowIndex;
                        }
                        coordinateMap.put(curColIndex, curRowIndex);
                    } else {
                        //如果当前列恰好是所需合并列的界限，记录下标
                        if (mergerColIndexLimit == curColIndex) {
                            mergerRowIndexLimit = curRowIndex;
                        }
                        coordinateMap.put(curColIndex, curRowIndex);
                    }

                }
            }
        }
        //如果是头部判断是否配置自动列宽配置

    }


}