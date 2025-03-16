/**
 * Created by Jacky.Gao on 2017-02-17.
 */
import { resetTableData, buildNewCellDef,undoManager} from '../../Utils.js';
import { insertRows, removeRows } from "./InsertRowOperation.js";
import {renderRowHeader} from '../HeaderUtils.js';

/** 禁用删除行菜单按钮 */
export function rowDeleteDisabled() {
  const selected = this.getSelected();
  if (!selected) {
    return true;
  }
  const startRow = selected[0],
    endRow = selected[2];
  let dif = Math.abs(startRow - endRow) + 1;
  const countRows = this.countRows();
  if (dif >= countRows) {
    return true;
  } else {
    return false;
  }
}

export function doDeleteRow() {
  const selected = this.getSelected(),
    context = this.context;
  if (!selected) {
    return;
  }
  let startRow = selected[0];
  let endRow = selected[2];
  let position = Math.min(startRow, endRow);
  let rowNum = Math.abs(endRow - startRow) + 1;
  const originMergeCells = JSON.parse(JSON.stringify(this.getSettings().mergeCells));
  const originRowHeaders = JSON.parse(JSON.stringify(context.rowHeaders));
  let removeCells = removeRows.call(this, position, rowNum);

  const _this = this;
  undoManager.add({
    redo: function () {
      removeCells = removeRows.call(_this, position, rowNum);
    },
    undo: function () {
      insertRows.call(_this, position, rowNum);
      // 恢复被删除的单元格
      removeCells.forEach((cell) => {
        context.addCell(cell);
      });
      // 恢复合并单元格配置
      let newMergeCells = JSON.parse(JSON.stringify(originMergeCells)); // alter插入行会影响合并单元格对象，需序列化
      _this.updateSettings({
        mergeCells: newMergeCells,
      });
      // 恢复行类型配置
      const rowHeaders = context.rowHeaders;
      rowHeaders.splice(0, rowHeaders.length);
      originRowHeaders.forEach((h) => {
        rowHeaders.push({ ...h });
      });
      renderRowHeader(context);
    },
  });
}
