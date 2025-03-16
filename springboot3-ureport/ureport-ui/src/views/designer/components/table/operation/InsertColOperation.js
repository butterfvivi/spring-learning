/**
 * Created by Jacky.Gao on 2017-02-17.
 */
import { resetTableData,buildNewCellDef,undoManager} from '../../Utils.js';

export function doInsertCol(elementId, left) {
  const insertColLeftSet = document.getElementById(elementId);
  let value = insertColLeftSet.value;
  if (!/^-?\d+$/.test(value)) {
    Message.error("请输入整数");
    return;
  }
  let colNum = parseInt(value);
  if (colNum <= 0) {
    Message.error("列数必须大于0");
    return;
  }
  if (colNum > 99) {
    Message.error("列数必须小于100");
    return;
  }

  const selected = this.getSelected();
  if (!selected) {
    return;
  }
  let startCol = selected[1];
  let endCol = selected[3];
  let position;
  if (left) {
    position = Math.min(startCol, endCol);
  } else {
    position = Math.max(startCol, endCol) + 1;
  }

  // 从position列开始插入colNum列
  insertCols.call(this, position, colNum);

  const _this = this;
  undoManager.add({
    redo: function () {
      insertCols.call(_this, position, colNum);
    },
    undo: function () {
      removeCols.call(_this, position, colNum);
      // 回显插入前选中的单元格
      //_this.selectCell(...selected);
    },
  });
}

/**
 * 从position列开始插入colNum列
 * @param {number} position 列索引
 * @param {number} colNum 插入列数
 * @param {number} colWidth 插入的列宽
 */
export function insertCols(position, colNum, colWidth = 106) {
  const { colWidths, mergeCells } = this.getSettings();
  let newColWidths = colWidths.concat([]);
  const context = this.context;

  // 更新合并单元格
  let newMergeCells = [];
  for (let mergeCell of mergeCells) {
    let newMergeCell = { ...mergeCell };
    if (newMergeCell.col >= position) {
      // 新增索引右侧的合并单元格右移
      newMergeCell.col += colNum;
    } else if (newMergeCell.col + newMergeCell.colspan - 1 >= position) {
      // 合并单元格尾列位于新增区域，扩展合并单元格
      newMergeCell.colspan += colNum;
    }
    newMergeCells.push(newMergeCell);
  }

  // 插入colNum列
  this.alter("insert_col", position, colNum);
  // 更新列宽
  newColWidths.splice(position, 0, ...new Array(colNum).fill(colWidth));

  // 从插入所在列开始，往右移动colNum格
  const changeCells = [];
  for (let cell of context.cellsMap.values()) {
    if (cell.columnNumber >= position + 1) {
      changeCells.push(cell);
      context.removeCell(cell);
    }
  }
  for (let cell of changeCells) {
    cell.columnNumber += colNum;
    context.addCell(cell);
  }

  // 插入区域创建新的单元格数据对象
  let countRows = this.countRows();
  for (let col = position + 1; col <= position + colNum; col++) {
    for (let row = 1; row <= countRows; row++) {
      let newCellDef = buildNewCellDef(row, col);
      context.addCell(newCellDef);
    }
  }

  this.updateSettings({
    mergeCells: newMergeCells,
    colWidths: newColWidths,
    manualColumnResize: newColWidths,
  });
  resetTableData(this);

  // 选中插入的单元格
  //this.selectCell(0, position, countRows - 1, position + colNum - 1);
}

/**
 * 从position列开始删除colNum列
 * @param {number} position 列索引
 * @param {number} colNum 删除列数
 * @return 删除的单元格列表
 */
export function removeCols(position, colNum) {
  const { colWidths, mergeCells } = this.getSettings();
  let newColWidths = colWidths.concat([]);
  const context = this.context;
  const removeCells = [];

  // 更新合并单元格
  let newMergeCells = [];
  for (let mergeCell of mergeCells) {
    let newMergeCell = { ...mergeCell };
    let colEnd = newMergeCell.col + newMergeCell.colspan - 1;
    if (newMergeCell.col >= position + colNum) {
      // 删除区域右侧的合并单元格左移
      newMergeCell.col -= colNum;
    } else if (newMergeCell.col >= position) {
      // 合并单元格首列位于删除区域
      let cutLen = position + colNum - newMergeCell.col;
      newMergeCell.colspan -= cutLen;
      newMergeCell.col = position;
      if (newMergeCell.colspan > 0) {
        // 将合并单元格配置移动到新的首列单元格中
        let cell = context.getCell(mergeCell.row, mergeCell.col);
        if (cell) {
          let newCell = JSON.parse(JSON.stringify(cell));
          newCell.columnNumber += cutLen;
          let targetCell = context.getCell(newCell.rowNumber - 1, newCell.columnNumber - 1);
          if (targetCell) {
            removeCells.push(targetCell); // 记录合并单元格内被替换的单元格，用于撤回
          }
          context.addCell(newCell);
        }
      }
    } else if (colEnd >= position + colNum) {
      // 合并单元格首尾跨越删除区域
      newMergeCell.colspan -= colNum;
    } else if (colEnd >= position) {
      // 合并单元格尾列位于删除区域
      newMergeCell.colspan -= colEnd - position + 1;
    }
    if (
      newMergeCell.rowspan > 0 &&
      newMergeCell.colspan > 0 &&
      (newMergeCell.rowspan > 1 || newMergeCell.colspan > 1)
    ) {
      // 截断后变为单个单元格，移除合并单元格
      newMergeCells.push(newMergeCell);
    }
  }

  // 删除colNum列
  this.alter("remove_col", position, colNum);
  // 更新列宽
  newColWidths.splice(position, colNum);

  // 删除单元格数据对象
  let countRows = this.countRows();
  for (let colIndex = position; colIndex < position + colNum; colIndex++) {
    for (let rowIndex = 0; rowIndex < countRows; rowIndex++) {
      let removeCell = context.getCell(rowIndex, colIndex);
      if (removeCell) {
        context.removeCell(removeCell);
        removeCells.push(removeCell);
      }
    }
  }

  // 从删除区域右侧开始，往左移动colNum格
  const changeCells = [];
  for (let cell of context.cellsMap.values()) {
    if (cell.columnNumber >= position + colNum + 1) {
      changeCells.push(cell);
      context.removeCell(cell);
    }
  }
  for (let cell of changeCells) {
    cell.columnNumber -= colNum;
    context.addCell(cell);
  }

  this.updateSettings({
    mergeCells: newMergeCells,
    colWidths: newColWidths,
    manualColumnResize: newColWidths,
  });
  resetTableData(this);

  return removeCells;
}
