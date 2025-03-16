import Handsontable from 'handsontable'
import { undoManager, debounce } from './Utils.js'

export default function dragField() {
  const sheetDiv = document.getElementById('container')
  document.ondragstart = function(ev) {
    ev.dataTransfer.setData('dataset', ev.target.attributes.dataset.nodeValue)
    ev.dataTransfer.setData('field', ev.target.textContent)
  }
  sheetDiv.ondragover = function(ev) {
    ev.preventDefault()
  }
  sheetDiv.ondrop = (ev) => {
    const ele = ev.target
    if (ele.nodeName.toLowerCase() !== 'td') {
      return false
    }
    const colIndex = parseInt(ele.getAttribute('c'))
    const rowIndex = parseInt(ele.getAttribute('r'))

    let cellDef = this.context.getCell(rowIndex, colIndex)
    const oldCellDef = JSON.parse(JSON.stringify(cellDef))
    const dataset = ev.dataTransfer.getData('dataset').trim()
    const field = ev.dataTransfer.getData('field').trim()

    if (cellDef.value.type !== 'dataset') {
      this.context.removeCell(cellDef)
      cellDef = {
        value: {
          type: 'dataset',
          conditions: []
        },
        rowNumber: cellDef.rowNumber,
        columnNumber: cellDef.columnNumber,
        cellStyle: cellDef.cellStyle
      }
      this.context.addCell(cellDef)
    }
    cellDef.expand = 'Down'
    const value = cellDef.value
    value.aggregate = 'group'
    value.datasetName = dataset
    value.property = field
    value.order = 'none'

    const text = `${value.datasetName}.${value.aggregate}(${value.property})`
    this.context.hot.setDataAtCell(rowIndex, colIndex, text)
    ev.preventDefault()
    const selected = this.context.hot.getSelected()
    if (selected) {
      const startRow = selected[0]
      const startCol = selected[1]
      const endRow = selected[2]
      const endCol = selected[3]
      this.refreshChildPropertyPanel(startRow, startCol, endRow, endCol)
    }
    const that = this
    undoManager.add({
      redo: function() {
        updateCellDef.call(that, rowIndex, colIndex, cellDef)
      },
      undo: function() {
        updateCellDef.call(that, rowIndex, colIndex, oldCellDef)
      }
    })
  }
}

function updateCellDef(rowIndex, colIndex, cellDef) {
  this.context.addCell(cellDef)
  const value = cellDef.value
  let text = ''
  if (cellDef.value.type !== 'dataset') {
    text = value.value
  } else {
    text = `${value.datasetName}.${value.aggregate}(${value.property})`
  }
  this.context.hot.setDataAtCell(rowIndex, colIndex, text)
  const selected = this.context.hot.getSelected()
  if (selected) {
    const startRow = selected[0]
    const startCol = selected[1]
    const endRow = selected[2]
    const endCol = selected[3]
    Handsontable.hooks.run(this.context.hot, 'afterSelectionEnd', startRow, startCol, endRow, endCol)
  }
}
