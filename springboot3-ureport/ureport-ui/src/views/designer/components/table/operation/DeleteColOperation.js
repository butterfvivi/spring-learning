/**
 * Created by Jacky.Gao on 2017-02-17.
 */
import {resetTableData,buildNewCellDef,undoManager} from '../../Utils.js'
import { insertCols, removeCols } from "./InsertColOperation.js";

/** 禁用删除列菜单按钮 */
export function colDeleteDisabled() {
  const selected = this.getSelected();
  if (!selected) {
    return true;
  }
  const startCol = selected[1],
    endCol = selected[3];
  let dif = Math.abs(startCol - endCol) + 1;
  const countCols = this.countCols();
  if (dif >= countCols) {
    return true;
  } else {
    return false;
  }
}

export function doDeleteCol() {
  const selected = this.getSelected();
  const context = this.context;
  if (!selected) {
    return;
  }
  let startCol = selected[1];
  let endCol = selected[3];
  let position = Math.min(startCol, endCol);
  let colNum = Math.abs(endCol - startCol) + 1;
  const originMergeCells = JSON.parse(JSON.stringify(this.getSettings().mergeCells));
  let removeCells = removeCols.call(this, position, colNum);

  const _this = this;
  undoManager.add({
    redo: function () {
      removeCells = removeCols.call(_this, position, colNum);
    },
    undo: function () {
      insertCols.call(_this, position, colNum);
      // 恢复被删除的单元格
      removeCells.forEach((cell) => {
        context.addCell(cell);
      });
      // 恢复合并单元格配置
      let newMergeCells = JSON.parse(JSON.stringify(originMergeCells)); // alter插入列会影响合并单元格对象，需序列化
      _this.updateSettings({
        mergeCells: newMergeCells,
      });
    },
  });
}