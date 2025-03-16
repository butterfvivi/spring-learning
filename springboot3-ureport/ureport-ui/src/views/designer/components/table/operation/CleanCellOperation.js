
import { undoManager } from '../../Utils.js'
import Handsontable from 'handsontable'

export function doCleanCell(type) {
  const _this = this
  const selected = this.getSelected()
  var startRow = selected[0]
  var endRow = selected[2]
  if (startRow > endRow) {
    var temp = endRow
    endRow = startRow
    startRow = temp
  }
  var startCol = selected[1]
  var endCol = selected[3]
  if (startCol > endCol) {
    var temp = endCol
    endCol = startCol
    startCol = temp
  }
  let removeCellsMap = cleanCells(_this.context, startRow, endRow, startCol, endCol, type);
  undoManager.add({
    redo: function() {
      removeCellsMap = cleanCells(_this.context, startRow, endRow, startCol, endCol, type);
    },
    undo: function() {
      undoCleanCells(_this.context, startRow, endRow, startCol, endCol, removeCellsMap, 'content');
    },
  })
}

function undoCleanCells(context, startRow, endRow, startCol, endCol, removeCellsMap, type) {
  let cellsMap = context.cellsMap,
  hot = context.hot;
  for (let i = startRow; i <= endRow; i++) {
    for (let j = startCol; j <= endCol; j++) {
      let cell = context.getCell(i, j);
      if (!cell) {
        continue;
      }
      let key = cell.rowNumber + "," + cell.columnNumber;
      if (type === 'content') {
        let orgValue = removeCellsMap.get(key);
        if (!orgValue) {
          //alert(`${window.i18n.table.contextMenu.cancelConetntFail}`);
          return;
        }
        cell.expand = orgValue.expand;
        cell.value = orgValue.value;
        let value = cell.value;
        let valueType = value.type;
        let text = value.value;
        if (valueType === 'dataset') {
          text = value.datasetName + "." + value.aggregate + "(" + value.property + ")";
        }
        //hot.setDataAtCell(i,j,text);
      } else if (type === 'style') {
        let orgStyle = removeCellsMap.get(key);
        if (!orgStyle) {
          //alert(`${window.i18n.table.contextMenu.cancelStyleFail}`);
          return;
        }
        cell.cellStyle = orgStyle.cellStyle;
      } else if (type === 'all') {
        context.removeCell(cell);
        let orgCell = removeCellsMap.get(key);
        if (!orgCell) {
          //alert(`${window.i18n.table.contextMenu.cancelClearFail}`);
          return;
        }
        context.addCell(orgCell);
        let value = orgCell.value;
        let valueType = value.type;
        let text = value.value;
        if (valueType === 'dataset') {
          text = value.datasetName + "." + value.aggregate + "(" + value.property + ")";
        }
        //hot.setDataAtCell(i,j,text);
      }
    }
  }
  Handsontable.hooks.run(hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol);
  hot.render();
};

function cleanCells(context, startRow, endRow, startCol, endCol, type) {
  let removeCellsMap = new Map()
  const hot = context.hot
  for (let i = startRow; i <= endRow; i++) {
    for (let j = startCol; j <= endCol; j++) {
      let cell = context.getCell(i, j)
      if (!cell) {
        continue;
      }
      cell.cellStyle.format = null
      let key = cell.rowNumber + "," + cell.columnNumber
      context.removeCell(cell)
      removeCellsMap.set(key, cell)
      if (type === 'content') {
        let newCell = {
          rowNumber: cell.rowNumber,
          columnNumber: cell.columnNumber,
          expand: 'None',
          remove: true,
          value: {
            type: 'simple',
            value: ''
          },
          cellStyle: cell.cellStyle
        }
        context.addCell(newCell)
      } else if (type === 'style') {
        let newCell = {
          rowNumber: cell.rowNumber,
          columnNumber: cell.columnNumber,
          expand: cell.expand,
          value: cell.value,
          cellStyle: {
            fontSize: 10,
            forecolor: '0,0,0',
            fontFamily: '宋体',
            align: 'left',
            valign: 'middle',
          }
        }
        context.addCell(newCell)
      } else if (type === 'all') {
        let newCell = {
          rowNumber: cell.rowNumber,
          columnNumber: cell.columnNumber,
          expand: 'None',
          remove: true,
          value: {
            type: 'simple',
            value: ''
          },
          cellStyle: {
            fontSize: 10,
            forecolor: '0,0,0',
            fontFamily: '宋体',
            align: 'left',
            valign: 'middle',
          }
        }
        context.addCell(newCell)
      }
    }
  }
  Handsontable.hooks.run(hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol);
  hot.render()
  return removeCellsMap
}


