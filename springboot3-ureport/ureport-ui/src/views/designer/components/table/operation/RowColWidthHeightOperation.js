import { Message } from 'element-ui'
import { undoManager, setDirty } from '../../Utils.js'

export function doRowColWidthHeight(elementId, type) {
  const input = document.getElementById(elementId)
  let value = input.value
  if(!/^-?\d+$/.test(value)) {
    Message({message: '请输入整数',type: 'error'})
    return
  }
  value = parseInt(value)
  if(type === 'col') {
    doColWidth.call(this, value)
  } else if(type === 'row') {
    doRowHeight.call(this, value)
  }
}

function doColWidth(newColWidth) {
  const selected = this.getSelected()
  const startRow = selected[0]
  const startCol = selected[1]
  const endCol = selected[3]
  const colWidths = this.getSettings().colWidths
  for(let i = startCol; i <= endCol; i++) {
    let td = this.getCell(startRow, i)
    if (td && td.style.display !== 'none') {
      colWidths[i] = newColWidth
    }
  }
  this.updateSettings({
    colWidths: colWidths,
    manualColumnResize: colWidths
  })
  const oldColWidths = this.context.colWidths.concat([])
  this.context.colWidths = colWidths.concat([])
  const hot = this
  undoManager.add({
    redo:function(){
      hot.updateSettings({
        colWidths:colWidths,
        manualColumnResize:colWidths
      })
      setDirty()
    },
    undo:function(){
      hot.updateSettings({
        colWidths:oldColWidths,
        manualColumnResize:oldColWidths
      })
      setDirty()
    }
  })
  setDirty()
}

function doRowHeight(newHeight) {
  const selected = this.getSelected()
  const startRow = selected[0]
  const startCol = selected[1]
  const endRow = selected[2]
  const rowHeights = this.getSettings().rowHeights
  for(let i = startRow; i <= endRow; i++) {
    let td = this.getCell(i, startCol)
    if (td && td.style.display !== 'none') {
      rowHeights[i] = newHeight
    }
  }
  this.updateSettings({
    rowHeights: rowHeights,
    manualRowResize: rowHeights
  })
  const oldRowHeights = this.context.rowHeights.concat([])
  this.context.rowHeights = rowHeights.concat([])
  const hot = this
  undoManager.add({
    redo:function(){
      hot.updateSettings({
        rowHeights:rowHeights,
        manualRowResize:rowHeights
      })
      setDirty()
    },
    undo:function(){
      hot.updateSettings({
        rowHeights:oldRowHeights,
        manualRowResize:oldRowHeights
      })
      setDirty()
    }
  })
  setDirty()
}
