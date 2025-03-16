/**
 * Created by Jacky.Gao on 2017-01-31.
 */
import Handsontable from 'handsontable'
import { renderRowHeader } from './HeaderUtils.js'
import { undoManager, resetTableData } from '../Utils.js'

import { doInsertRow } from './operation/InsertRowOperation.js'
import { doInsertCol } from './operation/InsertColOperation.js'
import { doDeleteRow } from './operation/DeleteRowOperation.js'
import { doDeleteCol } from './operation/DeleteColOperation.js'
import { doCleanCell } from './operation/CleanCellOperation.js'
import { doRepeatRow } from './operation/RepeatRowOperation.js'
import { doRowColWidthHeight } from './operation/RowColWidthHeightOperation.js'
import { copyCell, pasteCell, cleanCopyPasteDirty } from './operation/CopyPasteCellOperation.js'

export default function buildMenuConfigure() {
  return {
    callback: function(key, options) {
      const _this = this
      key !== 'paste' && cleanCopyPasteDirty.call(this)
      if (key === 'copy') {
        copyCell.call(this, false)
      } else if (key === 'cut') {
        copyCell.call(this, true)
      } else if (key === 'paste') {
        pasteCell.call(this)
      } else if (key === 'insert:insert_row_above') {
        doInsertRow.call(this, 'insertRowAboveSet', true)
      } else if (key === 'insert:insert_row_below') {
        doInsertRow.call(this, 'insertRowBelowSet')
      } else if (key === 'insert:insert_col_left') {
        doInsertCol.call(this, 'insertColLeftSet', true)
      } else if (key === 'insert:insert_col_right') {
        doInsertCol.call(this, 'insertColRightSet')
      } else if (key === 'delete:del_row') {
        doDeleteRow.call(this)
      } else if (key === 'delete:del_col') {
        doDeleteCol.call(this)
      } else if (key === 'clean_content') {
        doCleanCell.call(this, 'content')
      } else if (key === 'clean_style') {
        doCleanCell.call(this, 'style')
      } else if (key === 'clean') {
        doCleanCell.call(this, 'all')
      } else if (key === 'repeat:repeat_row_header') {
        doRepeatRow.call(this, 'headerrepeat')
      } else if (key === 'repeat:title_row') {
        doRepeatRow.call(this, 'title')
      } else if (key === 'repeat:repeat_row_footer') {
        doRepeatRow.call(this, 'footerrepeat')
      } else if (key === 'repeat:summary') {
        doRepeatRow.call(this, 'summary')
      } else if (key === 'repeat_cancel') {
        doRepeatRow.call(this, 'cancel')
      } else if (key === 'row_col:row_height') {
        doRowColWidthHeight.call(this, 'rowHeightSet', 'row')
      } else if (key === 'row_col:col_width') {
        doRowColWidthHeight.call(this, 'colWidthSet', 'col')
      }
    },
    items: {
      "copy": {
        name: '<svg style="width:16px;height:16px" xmlns="http://www.w3.org/2000/svg"><g fill="#3D4757"><path d="M6 1h8c.6 0 1 .4 1 1v9c0 .6-.4 1-1 1H6c-.6 0-1-.4-1-1V2c0-.6.4-1 1-1zm0 1v9h8V2H6z"/><path d="M5 4v1H3v9h8v-2h1v2c0 .6-.4 1-1 1H3c-.6 0-1-.4-1-1V5c0-.6.4-1 1-1h2z"/></g></svg>复制<span class="shortcut">Ctrl + C</span>',
      },
      "cut": {
        name: '<svg style="width:16px;height:16px" xmlns="http://www.w3.org/2000/svg"><g fill="#3D4757"><path d="M6.1 9.5l5.6-6.7c.2-.2.5-.2.7-.1.2.2.2.5.1.7l-6.7 8L4.4 13c-.7.8-2 1-2.8.2s-1-2-.2-2.8l1.3-1.5c.7-.8 2-1 2.8-.2.2.2.4.5.6.8zm-1.3-.1c-.4-.3-1-.3-1.4.1L2.1 11c-.4.4-.3 1.1.1 1.4s1.1.3 1.4-.1l1.3-1.5c.4-.4.3-1-.1-1.4z"/><path d="M9.8 9.5L4.2 2.8c-.2-.2-.5-.3-.7-.1s-.3.5-.1.7l6.7 8 1.3 1.5c.7.8 2 1 2.8.2s1-2 .2-2.8l-1.3-1.5c-.7-.8-2-1-2.8-.2-.2.3-.4.6-.5.9zm1.2-.1c.4-.4 1.1-.3 1.4.1l1.3 1.5c.4.4.3 1.1-.1 1.4-.4.4-1.1.3-1.4-.1l-1.3-1.5c-.3-.4-.3-1 .1-1.4z"/></g></svg>剪切<span class="shortcut">Ctrl + X</span>',
      },
      "paste": {
        name: '<svg style="width:16px;height:16px" xmlns="http://www.w3.org/2000/svg"><g fill="#3D4757"><path d="M15 12h-1V8H6v6h6v1H6c-.6 0-1-.4-1-1V8c0-.6.4-1 1-1h8c.6 0 1 .4 1 1v4z"/><path d="M5 12v1H3c-.6 0-1-.4-1-1V3c0-.6.4-1 1-1h1v1H3v9h2zm6-5V3H9V2h2c.6 0 1 .4 1 1v4h-1z"/><path d="M12 15l3-3h-2.5c-.3 0-.5.2-.5.5V15z"/><path d="M4 1h6v3H4V1zm1 1v1h4V2H5z"/></g></svg>粘贴<span class="shortcut">Ctrl + V</span>',
      },
      'separator': Handsontable.plugins.ContextMenu.SEPARATOR,
      "insert": {
        name: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" x="7.5" y="4.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M3 6v1h2V6zm-3 .5L3 4v5zM0 0h12v1H0zm0 12h12v1H0z"/></g></svg>插入',
        submenu: {
          items: [
            {
              key: 'insert:insert_row_above',
              name:'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" x="7.5" y="4.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M3 6v1h2V6zm-3 .5L3 4v5zM0 0h12v1H0zm0 12h12v1H0z"/></g></svg>插入行(上)<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="insertRowAboveSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus disabled"></span></div></div>'
            },
            {
              key: 'insert:insert_row_below',
              name:'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" x="7.5" y="4.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M3 6v1h2V6zm-3 .5L3 4v5zM0 0h12v1H0zm0 12h12v1H0z"/></g></svg>插入行(下)<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="insertRowBelowSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus disabled"></span></div></div>'
            },
            {
              key: 'insert:insert_col_left',
              name:'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" transform="rotate(90 6.5 10.5)" x="3.5" y="8.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M7 3H6v2h1zm-.5-3L9 3H4zM1 0v12H0V0zm12 0v12h-1V0z"/></g></svg>插入列(左)<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="insertColLeftSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus disabled"></span></div></div>'
            },
            {
              key: 'insert:insert_col_right',
              name:'<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" transform="rotate(90 6.5 10.5)" x="3.5" y="8.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M7 3H6v2h1zm-.5-3L9 3H4zM1 0v12H0V0zm12 0v12h-1V0z"/></g></svg>插入列(右)<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="insertColRightSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus disabled"></span></div></div>'
            },
          ]
        }
      },
      "delete": {
        name: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#909AA9" x="7.5" y="4.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M.625 4L5 8.375 4.375 9 0 4.625z"/><path fill="#3D4757" d="M0 8.375L4.375 4 5 4.625.625 9zM0 12h12v1H0zM0 0h12v1H0z"/></g></svg>删除',
        submenu: {
          items: [
            {
              key: 'delete:del_row',
              name: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#909AA9" x="7.5" y="4.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M.625 4L5 8.375 4.375 9 0 4.625z"/><path fill="#3D4757" d="M0 8.375L4.375 4 5 4.625.625 9zM0 12h12v1H0zM0 0h12v1H0z"/></g></svg>删除行`,
              disabled: checkRowDeleteOperationDisabled
            },
            {
              key: 'delete:del_col',
              name: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><rect stroke="#909AA9" transform="rotate(90 6.5 10.5)" x="3.5" y="8.5" width="6" height="4" rx="1"/><path fill="#3D4757" d="M4.625 0L9 4.375 8.375 5 4 .625z"/><path fill="#3D4757" d="M4 4.375L8.375 0 9 .625 4.625 5zM1 0v12H0V0zm12 0v12h-1V0z"/></g></svg>删除列`,
              disabled: checkColDeleteOperationDisabled
            }
          ]
        }
      },
      "row_col": {
        name: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g fill="none" fill-rule="evenodd"><path fill="#3D4757" d="M2 12h11v1H2z"/><rect stroke="#3D4757" x="2.5" y="5.5" width="7" height="4" rx="1"/><path fill="#3D4757" d="M2 2h11v1H2zm12 5.5L11 10V5z"/></g></svg>行列',
        submenu: {
          items: [
            {
              key: 'row_col:row_height',
              name: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(2 1)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" transform="rotate(90 9 6.5)" x="3" y="4" width="12" height="5" rx="1"/><path fill="#3D4757" d="M2 0v2H1V0zm0 9v1H1V9zm0 2v1H1v-1zm0-4v1H1V7zm0-2v1H1V5zm0-2v1H1V3z"/><path fill="#3D4757" d="M0 0h3v1H0zm0 12h3v1H0z"/></g></svg>设置行高<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="rowHeightSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus"></span></div></div>`,
              disabled: checkRowDeleteOperationDisabled
            },
            {
              key: 'row_col:col_width',
              name: `<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 2)" fill="none" fill-rule="evenodd"><rect stroke="#3D4757" transform="rotate(-180 6.5 9)" x=".5" y="6.5" width="12" height="5" rx="1"/><path fill="#3D4757" d="M10 2H9V1h1zm2 0h-1V1h1zM2 2H0V1h2zm2 0H3V1h1zm2 0H5V1h1zm2 0H7V1h1zm5-2v3h-1V0z"/><path fill="#3D4757" d="M1 0v3H0V0z"/></g></svg>设置列宽<div class="et-input-number" onmousedown="event.stopPropagation()"><div class="input-number-wrap"><input id="colWidthSet" value="1"></div><div class="input-btns-wrap"><span class="input-btn add"></span><span class="input-btn minus"></span></div></div>`,
              disabled: checkColDeleteOperationDisabled
            },
          ]
        }
      },
      'separator1': Handsontable.plugins.ContextMenu.SEPARATOR,
      "repeat": {
        name: '设置行类型',
        submenu: {
          items: [
            {
              key: 'repeat:title_row',
              name:'标题行',
              disabled: checkRowDeleteOperationDisabled
            },
            {
              key: 'repeat:repeat_row_header',
              name:'重复表头',
              disabled: checkRowDeleteOperationDisabled
            },
            {
              key: 'repeat:repeat_row_footer',
              name: `重复表尾`,
              disabled: checkRowDeleteOperationDisabled
            },
            {
              key: 'repeat:summary',
              name:'总结行',
              disabled: checkRowDeleteOperationDisabled
            },
          ]
        }
      },
      "repeat_cancel": {
        name: `取消行类型`,
        disabled: checkRowDeleteOperationDisabled
      },
      'separator3': Handsontable.plugins.ContextMenu.SEPARATOR,
      "clean_content": {
        name: '清除内容',
      },
      "clean_style": {
        name: '清除格式',
      }
    }
  };

  function undoPasteStyle(context, startRow, endRow, startCol, endCol, oldStyleMap) {
    const style = window.__copy_cell_style__;
    let cellsMap = new Map(),
      hot = context.hot;
    for (let i = startRow; i <= endRow; i++) {
      for (let j = startCol; j <= endCol; j++) {
        let cell = context.getCell(i, j);
        if (!cell) {
          continue;
        }
        let key = cell.rowNumber + "," + cell.columnNumber;
        const oldStyle = oldStyleMap.get(key);
        if (oldStyle) {
          cell.cellStyle = oldStyle;
        }
      }
    }
    Handsontable.hooks.run(hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol);
    hot.render();
    return cellsMap;
  };

  function pasteStyle(context, startRow, endRow, startCol, endCol) {
    const style = window.__copy_cell_style__
    let cellsMap = new Map()
    const hot = context.hot
    for (let i = startRow; i <= endRow; i++) {
      for (let j = startCol; j <= endCol; j++) {
        let cell = context.getCell(i, j);
        if (!cell) {
          continue;
        }
        let key = cell.rowNumber + "," + cell.columnNumber;
        if (!cell.cellStyle) {
          cell.cellStyle = {};
        }
        const oldStyle = JSON.parse(JSON.stringify(cell.cellStyle));
        cellsMap.set(key, oldStyle);
        cell.cellStyle.fontSize = style.fontSize;
        cell.cellStyle.forecolor = style.forecolor;
        cell.cellStyle.fontFamily = style.fontFamily;
        cell.cellStyle.valign = style.valign;
        cell.cellStyle.align = style.align;
        cell.cellStyle.bgcolor = style.bgcolor;
        cell.cellStyle.bold = style.bold;
        cell.cellStyle.italic = style.italic;
        cell.cellStyle.underline = style.underline;
        if (style.leftBorder) {
          cell.cellStyle.leftBorder = style.leftBorder
        }
        if (style.rightBorder) {
          cell.cellStyle.rightBorder = style.rightBorder
        }
        if (style.topBorder) {
          cell.cellStyle.topBorder = style.topBorder
        }
        if (style.bottomBorder) {
          cell.cellStyle.bottomBorder = style.bottomBorder
        }
      }
    }
    Handsontable.hooks.run(hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol);
    hot.render();
    return cellsMap;
  };

  function checkCopyOperationDisabled() {
    const selected = this.getSelected();
    if (!selected) {
      return true;
    }
    return false;
  };

  function checkPasteOperationDisabled() {
    const selected = this.getSelected();
    if (!selected) {
      return true;
    }
    if (window.__copy_cell_style__) {
      return false;
    }
    return true;
  };

  function checkRowDeleteOperationDisabled() {
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
  };

  function checkColDeleteOperationDisabled() {
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
  };

  function checkCleanOperationDisabled() {
    const selected = this.getSelected();
    if (!selected || selected.length === 0) {
      return true;
    }
    return false;
  };
}
