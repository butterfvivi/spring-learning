/**
 * Created by Jacky.Gao on 2017-02-17.
 */
import { resetTableData, buildNewCellDef, undoManager} from '../../Utils.js'
import { adjustInsertRowHeaders, adjustDelRowHeaders, renderRowHeader} from '../HeaderUtils.js'

export function doInsertRow(elementId, above) {
  const input = document.getElementById(elementId);
  let value = input.value;
  if (!/^-?\d+$/.test(value)) {
    Message.error("请输入整数");
    return;
  }
  let rowNum = parseInt(value);
  if (rowNum <= 0) {
    Message.error("行数必须大于0");
    return;
  }
  if (rowNum > 99) {
    Message.error("行数必须小于100");
    return;
  }

  const selected = this.getSelected();
  if (!selected) {
    return;
  }
  let startRow = selected[0];
  let endRow = selected[2];
  let position;
  if (above) {
    position = Math.min(startRow, endRow);
  } else {
    position = Math.max(startRow, endRow) + 1;
  }

  // 从position行开始插入rowNum行
  insertRows.call(this, position, rowNum);

  const _this = this;
  undoManager.add({
    redo: function () {
      insertRows.call(_this, position, rowNum);
    },
    undo: function () {
      removeRows.call(_this, position, rowNum);
      // 回显插入前选中的单元格
      //_this.selectCell(...selected);
    },
  });
}

/**
 * 从position行开始插入rowNum行
 * @param {number} position 行索引
 * @param {number} rowNum 插入行数
 * @param {number} rowHeight 插入的行高
 */
export function insertRows(position, rowNum, rowHeight = 24) {
  const { rowHeights, mergeCells } = this.getSettings();
  let newRowHeights = rowHeights.concat([]);
  const context = this.context;

  // 更新合并单元格
  let newMergeCells = [];
  for (let mergeCell of mergeCells) {
    let newMergeCell = { ...mergeCell };
    if (newMergeCell.row >= position) {
      // 新增索引下方的合并单元格下移
      newMergeCell.row += rowNum;
    } else if (newMergeCell.row + newMergeCell.rowspan - 1 >= position) {
      // 合并单元格尾行位于新增区域，扩展合并单元格
      newMergeCell.rowspan += rowNum;
    }
    newMergeCells.push(newMergeCell);
  }

  // 插入rowNum行
  this.alter("insert_row", position, rowNum);
  // 更新行类型
  adjustInsertRowHeaders(context, position, rowNum);
  renderRowHeader(this,context);
  // 更新行高
  newRowHeights.splice(position, 0, ...new Array(rowNum).fill(rowHeight));

  // 从插入所在行开始，往下移动rowNum格
  const changeCells = [];
  for (let cell of context.cellsMap.values()) {
    if (cell.rowNumber >= position + 1) {
      changeCells.push(cell);
      context.removeCell(cell);
    }
  }
  for (let cell of changeCells) {
    cell.rowNumber += rowNum;
    context.addCell(cell);
  }

  // 插入区域创建新的单元格数据对象
  const countCols = this.countCols();
  for (let row = position + 1; row <= position + rowNum; row++) {
    for (let col = 1; col <= countCols; col++) {
      let newCellDef = buildNewCellDef(row, col);
      context.addCell(newCellDef);
    }
  }

  this.updateSettings({
    mergeCells: newMergeCells,
    rowHeights: newRowHeights,
    manualRowResize: newRowHeights,
  });
  resetTableData(this);
  // 选中插入的单元格
  //this.selectCell(position, 0, position + rowNum - 1, countCols - 1);
}

/**
 * 从position行开始删除rowNum行
 * @param {number} position 行索引
 * @param {number} rowNum 删除行数
 * @return 删除的单元格列表
 */
export function removeRows(position, rowNum) {
  const { rowHeights, mergeCells } = this.getSettings();
  let newRowHeights = rowHeights.concat([]);
  const context = this.context;
  const removeCells = [];

  // 更新合并单元格
  let newMergeCells = [];
  for (let mergeCell of mergeCells) {
    let newMergeCell = { ...mergeCell };
    let rowEnd = newMergeCell.row + newMergeCell.rowspan - 1;
    if (newMergeCell.row >= position + rowNum) {
      // 删除区域下方的合并单元格上移
      newMergeCell.row -= rowNum;
    } else if (newMergeCell.row >= position) {
      // 合并单元格首行位于删除区域
      let cutLen = position + rowNum - newMergeCell.row;
      newMergeCell.rowspan -= cutLen;
      newMergeCell.row = position;
      if (newMergeCell.rowspan > 0) {
        // 将合并单元格配置移动到新的首行单元格中
        let cell = context.getCell(mergeCell.row, mergeCell.col);
        if (cell) {
          let newCell = JSON.parse(JSON.stringify(cell));
          newCell.rowNumber += cutLen;
          let targetCell = context.getCell(newCell.rowNumber - 1, newCell.columnNumber - 1);
          if (targetCell) {
            removeCells.push(targetCell); // 记录合并单元格内被替换的单元格，用于撤回
          }
          context.addCell(newCell);
        }
      }
    } else if (rowEnd >= position + rowNum) {
      // 合并单元格首尾跨越删除区域
      newMergeCell.rowspan -= rowNum;
    } else if (rowEnd >= position) {
      // 合并单元格尾行位于删除区域
      newMergeCell.rowspan -= rowEnd - position + 1;
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

  // 删除rowNum行
  this.alter("remove_row", position, rowNum);
  // 更新行类型
  adjustDelRowHeaders(context, position, rowNum);
  renderRowHeader(this,context);
  // 更新行高
  newRowHeights.splice(position, rowNum);

  // 删除单元格数据对象
  let countCols = this.countCols();
  for (let rowIndex = position; rowIndex < position + rowNum; rowIndex++) {
    for (let colIndex = 0; colIndex < countCols; colIndex++) {
      let removeCell = context.getCell(rowIndex, colIndex);
      if (removeCell) {
        context.removeCell(removeCell);
        removeCells.push(removeCell);
      }
    }
  }

  // 从删除区域下方开始，往上移动colNum格
  const changeCells = [];
  for (let cell of context.cellsMap.values()) {
    if (cell.rowNumber >= position + rowNum + 1) {
      changeCells.push(cell);
      context.removeCell(cell);
    }
  }
  for (let cell of changeCells) {
    cell.rowNumber -= rowNum;
    context.addCell(cell);
  }

  this.updateSettings({
    mergeCells: newMergeCells,
    rowHeights: newRowHeights,
    manualRowResize: newRowHeights,
  });
  resetTableData(this);

  return removeCells;
}
