/**
 * Created by Jacky.Gao on 2017-01-26.
 */
import * as utils from '../Utils.js'
import {afterRenderer} from './CellRenderer.js'
import buildMenuConfigure from './ContextMenu.js'
import Handsontable from 'handsontable'
import { getFileData } from '@/api/designer'
import { copyCell, pasteCell } from './operation/CopyPasteCellOperation.js'
import { doCleanCell } from './operation/CleanCellOperation.js'
import { Message } from 'element-ui'

export default class ReportTable {
  constructor(reportName, container, callback) {
    this.container = container
    this.hot = new Handsontable(container,{
      startCols:1,
      startRows:1,
      fillHandle:{
        autoInsertRow:false
      },
      copyPaste: false, // 允许粘贴
      colHeaders:true,
      rowHeaders:true,
      autoColumnSize:false,
      autoRowSize:false,
      manualColumnResize:true,
      manualRowResize:true,
      maxColsNumber:700,
      outsideClickDeselects:false,
      contextMenu: buildMenuConfigure()
    })
    this.hot.addHook("afterContextMenuShow",afterContextMenuShow)
    this.hot.addHook("afterBeginEditing",afterBeginEditing)
    this.hot.addHook("beforeKeyDown",beforeKeyDown)
    this.hot.addHook("afterChange",afterChange)
    this.hot.addHook("afterRenderer",afterRenderer)
    this.hot.addHook('afterRowResize', afterRowResize)
    this.hot.addHook('afterColumnResize',afterColumnResize)

    this.cellsMap = new Map()
    this.callback = callback
    this.loadFile(reportName)
  }

  loadFile(file){
    getFileData({file}).then(res => {
      this.loadData(res.data)
    })
  }

  loadData(data) {
    const reportDef = data
    utils.undoManager.clear()
    this.clearFloatImages()
    this.setBackGroundImage(reportDef.paper.bgImage)
    this.reportDef = reportDef
    if(this.callback){
      this.callback.call(this,reportDef)
    }
    this.buildReportData(reportDef)
    const selected = this.hot.getSelected()
    if (selected) {
      const startRow = selected[0]
      const startCol = selected[1]
      const endRow = selected[2]
      const endCol = selected[3]
      Handsontable.hooks.run(this.hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol);
    }
    this.hot.render()
  }

  clearFloatImages() {
    const block = document.getElementById('float_image_block')
    if(block) {
      block.innerHTML = ''
    }
  }

  buildReportData(data){
    this.cellsMap.clear()
    const rows = data.rows || []
    for(let i = rows.length + 1; i< 41; i++) {
        rows.push({
        rowNumber: i,
        height: 18
      })
    }
    const rowHeights = []
    for(let row of rows){
      const height = row.height
      rowHeights.push(utils.pointToPixel(height))
    }
    const columns = data.columns
    for(let i = columns.length + 1; i< 22; i++) {
        columns.push({
        columnNumber: i,
        hide: false,
        width: 80
      })
    }
    const colWidths = []
    for(let col of columns){
      const width = col.width
      colWidths.push(utils.pointToPixel(width))
    }
    const cellsMap = data.cellsMap
    const dataArray = [],mergeCells = []
    for(let row of rows) {
      const rowData = []
      for(let col of columns) {
        let key = row.rowNumber + "," + col.columnNumber
        let cell = cellsMap[key]
        if(cell){
            this.cellsMap.set(key,cell)
            rowData.push(cell.value.value || "")
            let rowspan = cell.rowSpan,colspan=cell.colSpan
            if(rowspan > 0 || colspan > 0) {
              if(rowspan===0) {
                rowspan=1
              }
              if(colspan===0) {
                colspan=1
              }
              mergeCells.push({
                rowspan,
                colspan,
                row:row.rowNumber - 1,
                col:col.columnNumber - 1
              })
            }
        } else {
          cell = utils.buildNewCellDef(row.rowNumber, col.columnNumber)
          this.cellsMap.set(key,cell)
          rowData.push('');
        }
      }
      dataArray.push(rowData)
    }
    this.hot.loadData(dataArray)
    this.hot.context.rowHeights = rowHeights.concat([])
    this.hot.context.colWidths = colWidths.concat([])
    this.hot.updateSettings({
      colWidths: colWidths,
      manualColumnResize: colWidths,
      rowHeights: rowHeights,
      manualRowResize: rowHeights,
      mergeCells,
      readOnly:false
    })
  }

  setBackGroundImage(url) {
    const ele = document.getElementById('designer')
    const master = ele.children[0]
    const wtHolder = master.children[0]
    const wtHider = wtHolder.children[0]
    if (url) {
      wtHider.style.background = 'url(' + url + ') 52px 27px no-repeat'
    } else {
      wtHider.style.background = 'transparent'
    }
  }

  bindSelectionEvent(callback){
    Handsontable.hooks.add("afterSelectionEnd",function(rowIndex,colIndex,row2Index,col2Index){
      callback.call(this,rowIndex,colIndex,row2Index,col2Index)
    },this.hot)
  }
}

function afterBeginEditing(changes,source) {
  utils.setDirty()
}

function beforeKeyDown(e) {
  if(e.ctrlKey) {
    if(e.code === 'KeyC') {
      copyCell.call(this, false)
    } else if(e.code === 'KeyX') {
      copyCell.call(this, true)
    } else if(e.code === 'KeyV') {
      pasteCell.call(this)
    } else if(e.code === 'KeyS') {
      event.preventDefault()
    } else if(e.code === 'KeyZ') {
      utils.undoManager.hasUndo() ? utils.undoManager.undo() : Message.info('当前没有内容可以撤销')
    } else if(e.code === 'KeyY') {
      utils.undoManager.hasRedo() ? utils.undoManager.redo() : Message.info('当前没有内容可以恢复')
    }
  } else if(e.code === 'Delete') {
    doCleanCell.call(this,'content')
    utils.setDirty()
  }
}

function afterChange(changes,source) {
  if(!changes || changes.length === 0){
    return
  }
  const arr = changes[0]
  const rowIndex = arr[0]
  const colIndex = arr[1]
  const oldValue = arr[2]
  const newValue = arr[3]
  if(oldValue === newValue) {
    return
  }
  const cellDef = this.context.getCell(rowIndex,colIndex)
  if (!cellDef || cellDef.value.type !== 'simple') {
    return
  }
  cellDef.value.value = newValue
  this.setDataAtCell(rowIndex, colIndex, newValue)
  const that = this
  utils.undoManager.add({
    redo:function(){
      cellDef.value.value = newValue
      that.setDataAtCell(rowIndex, colIndex, newValue)
      that.context.refreshSelected()
    },
    undo:function(){
      cellDef.value.value = oldValue
      that.setDataAtCell(rowIndex, colIndex, oldValue)
      that.context.refreshSelected()
    }
  })
  that.context.refreshSelected()
}

function afterRowResize(currentRow,newSize) {
  const rowHeights = this.getSettings().rowHeights
  const oldRowHeights = this.context.rowHeights.concat([])
  this.context.rowHeights = rowHeights.concat([])
  const hot = this
  utils.undoManager.add({
    redo:function(){
      hot.updateSettings({
        rowHeights:rowHeights,
        manualRowResize:rowHeights
      })
      utils.setDirty()
    },
    undo:function(){
      hot.updateSettings({
        rowHeights:oldRowHeights,
        manualRowResize:oldRowHeights
      })
      utils.setDirty()
    }
  })
  utils.setDirty()
}

function afterColumnResize(currentColumn,newSize){
  let colWidths = this.getSettings().colWidths
  const oldColWidths = this.context.colWidths.concat([])
  this.context.colWidths = colWidths.concat([])
  const hot = this
  utils.undoManager.add({
    redo:function(){
      hot.updateSettings({
        colWidths:colWidths,
        manualColumnResize:colWidths
      })
      utils.setDirty()
    },
    undo:function(){
      hot.updateSettings({
        colWidths:oldColWidths,
        manualColumnResize:oldColWidths
      })
      utils.setDirty()
    }
  })
  utils.setDirty()
}

function afterContextMenuShow(e) {
  let submenu = e.menu.menuItems[6].submenu
  let row = submenu.items[0]
  let col = submenu.items[1]
  const selected = this.getSelected()
  const startRow = selected[0]
  const startCol = selected[1]
  const endRow = selected[2]
  const colWidth = this.getColWidth(startCol)
  const rowHeight = this.getRowHeight(startRow)
  row.name = row.name.replace(/value="\d+"/,`value="${rowHeight}"`)
  col.name = col.name.replace(/value="\d+"/,`value="${colWidth}"`)
}
