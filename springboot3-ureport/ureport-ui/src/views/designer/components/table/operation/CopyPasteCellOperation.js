import Handsontable from 'handsontable'
import { Message } from 'element-ui'
import { undoManager, buildNewCellDef } from '../../Utils.js'
import { CellRange } from '../CellRange.js'
import { insertRows } from './InsertRowOperation.js'
import { insertCols } from './InsertColOperation.js'

let copyData = null;
export function copyCell(cut) {
  const {rowIndex,colIndex,row2Index,col2Index} = this.context.selectionCellRange()
  const mergeCells = this.getSettings().mergeCells || []
  const mergeIndex = {}
  const mergeExcludeIndex = {}
  for(var k = 0; k < mergeCells.length; k++) {
    let {row, col, rowspan, colspan} = mergeCells[k]
    for(var i = row; i < row + rowspan; i++) {
      for(var j = col; j < col + colspan; j++) {
        if(i === row && j === col) {
          mergeIndex[i + ',' + j] = {rowspan, colspan}
          continue
        }
        mergeExcludeIndex[i + ',' + j] = true
      }
    }
  }
  copyData = {
    cut: cut,
    rowIndex: rowIndex,
    colIndex: colIndex,
    row2Index: row2Index,
    col2Index: col2Index,
    data: []
  }
  var index = 0
  for(let i = rowIndex; i <= row2Index; i++) {
    copyData.data.push([])
    for(let j = colIndex; j <= col2Index; j++) {
      if(!mergeExcludeIndex[i + ',' + j]) {
        let cell = this.context.getCell(i, j);
        if (cell) {
          cell = JSON.parse(JSON.stringify(cell))
          let merge = mergeIndex[i + ',' + j]
          if(merge) {
            cell.rowspan = merge.rowspan
            cell.colspan = merge.colspan
          }
          copyData.data[index].push(cell)
        }
      }
    }
    index++
  }
  if(rowIndex === row2Index && colIndex === col2Index) {
    buildecurrentCellAnimation()
  } else {
    let m = mergeIndex[rowIndex + ',' + colIndex]
    if(m) {
      if(rowIndex + m.rowspan - 1 === row2Index && colIndex + m.colspan - 1 === col2Index) {
        buildecurrentCellAnimation()
      }  else {
        buildecurrentAreaAnimation()
      }
    } else {
      buildecurrentAreaAnimation()
    }
  }
}

export function pasteCell() {
  if(!copyData || copyData.data.length === 0) {
    return
  }
  const {rowIndex,colIndex,row2Index,col2Index} = this.context.selectionCellRange()
  const mergeCells = this.getSettings().mergeCells || [];

  let clean = false
  const copyRange = new CellRange(
    copyData.rowIndex,
    copyData.colIndex,
    copyData.row2Index,
    copyData.col2Index
  );
  let pasteEndRowIdx = rowIndex + (copyData.row2Index - copyData.rowIndex)
  let pasteEndColIdx = colIndex + (copyData.col2Index - copyData.colIndex)
  const pasteRange = new CellRange(rowIndex, colIndex, pasteEndRowIdx, pasteEndColIdx)

  // 若粘贴区域就是复制区域，则不做操作
  if (copyRange.equals(pasteRange)) {
    return
  }
  // 若粘贴区域与复制区域重叠了，则清空复制区域
  if (pasteRange.intersects(copyRange)) {
    clean = true
  }

  let firstCell = copyData.data[0][0]
  let rowOffset = rowIndex - (firstCell.rowNumber - 1)
  let colOffset = colIndex - (firstCell.columnNumber - 1)
  let sameCellMerge = false
  if(isSameCellMerge(rowOffset, colOffset, mergeCells, this.context)) {
    sameCellMerge = true
  } else {
    const s = new CellRange(copyData.rowIndex, copyData.colIndex, copyData.row2Index, copyData.col2Index);
    const d = new CellRange(rowIndex, colIndex, row2Index, col2Index);
    if(!canPaste(s, d, mergeCells)) {
      Message({message: '不能对合并单元格作部分修改',type: 'error'})
      return
    }
  }
  // 若粘贴后超出边界，则插入行、列
  var rowCount = this.countRows()
  if (pasteEndRowIdx + 1 > rowCount) {
    insertRows.call(this, rowCount, pasteEndRowIdx + 1 - rowCount)
  }
  var colCount = this.countCols()
  if (pasteEndColIdx + 1 > colCount) {
    insertCols.call(this, colCount, pasteEndColIdx + 1 - colCount)
  }
  if(copyData.cut) {
    for (let i = copyData.rowIndex; i <= copyData.row2Index; i++) {
      for (let j = copyData.colIndex; j <= copyData.col2Index; j++) {
        let cell = this.context.getCell(i, j)
        if (!cell) {
          continue;
        }
        let newCell = buildNewCellDef(cell.rowNumber, cell.columnNumber)
        this.context.addCell(newCell)
      }
    }
  }
  const oldMergeCells = JSON.parse(JSON.stringify(mergeCells))
  const oldCells = []
  const newCells = []
  for(var i = 0; i < copyData.data.length; i++) {
    let row = copyData.data[i]
    for(var j = 0; j < row.length; j++) {
      let cell = row[j]
      let r = cell.rowNumber - 1 + rowOffset
      let c = cell.columnNumber - 1 + colOffset
      if(cell.rowspan && cell.colspan && !sameCellMerge) {
        const newMergeItem = {
          row: r,
          col: c,
          rowspan: cell.rowspan,
          colspan: cell.colspan
        }
        mergeCells.push(newMergeItem)
      }
      let targetCell = this.context.getCell(r, c);
      if(targetCell) {
        oldCells.push({
          "cell": targetCell,
          "value": targetCell.value,
          "cellStyle": targetCell.cellStyle
        })
        targetCell.value = JSON.parse(JSON.stringify(cell.value))
        targetCell.expand = cell.expand
        targetCell.cellStyle = JSON.parse(JSON.stringify(cell.cellStyle))
        newCells.push({
          "cell": targetCell,
          "value": targetCell.value,
          "cellStyle": targetCell.cellStyle
        })
      }
    }
  }
  if(copyData.cut || clean) {
    cleanCopyPasteDirty()
  }
  this.updateSettings({
    mergeCells
  })
  Handsontable.hooks.run(this, 'afterSelectionEnd', rowIndex, colIndex, row2Index, col2Index);
  this.render()
  const that = this
  undoManager.add({
    redo: function() {
      that.updateSettings({
        mergeCells
      })
      for(let i = 0; i < newCells.length; i++) {
        let newCell = newCells[i]
        newCell.cell.value = newCell.value
        newCell.cell.cellStyle = newCell.cellStyle
      }
      Handsontable.hooks.run(that, 'afterSelectionEnd', rowIndex, colIndex, row2Index, col2Index);
      that.render()
    },
    undo: function() {
      that.updateSettings({
        mergeCells:oldMergeCells
      })
      for(let i = 0; i < oldCells.length; i++) {
        let oldCell = oldCells[i]
        oldCell.cell.value = oldCell.value
        oldCell.cell.cellStyle = oldCell.cellStyle
      }
      Handsontable.hooks.run(that, 'afterSelectionEnd', rowIndex, colIndex, row2Index, col2Index);
      that.render()
    },
  })
}

export function cleanCopyPasteDirty() {
  copyData = null
  let ele = document.getElementById('copy_border_block')
  if(ele) {
    ele.innerHTML = ''
  }
}

function buildecurrentCellAnimation() {
  let ele = document.getElementById('copy_border_block')
  let htBorders = ele.parentNode
  let wtSpreader = htBorders.parentNode
  let wtBorders = htBorders.querySelectorAll('.current')
  let left = parseInt(wtBorders[1].style.left) + 2
  let height = parseInt(wtBorders[1].style.height)
  let top = parseInt(wtBorders[0].style.top) + 1
  let width = parseInt(wtBorders[0].style.width)
  ele.style.left = left + 'px'
  ele.style.top = top + 'px'
  ele.setAttribute("data-target-top",top + parseInt(wtSpreader.style.top))
  ele.setAttribute("data-target-left",left + parseInt(wtSpreader.style.left))
  ele.innerHTML = `<svg id="dash_spin" width="${width}px" height="${height}px" style="pointer-events: none;"><path id="line_green" stroke="#1FBB7D" stroke-width="2" fill="none" d="m0,0 v${height} h${width} v-${height} h-${width} z"></path><path id="dash_white" stroke="#FFFFFF" stroke-width="2" fill="none" stroke-dasharray="6" stroke-dashoffset="8" d="m0,0 v${height} h${width} v-${height} h-${width} z"></path></svg>`
}

function buildecurrentAreaAnimation() {
  let ele = document.getElementById('copy_border_block')
  let htBorders = ele.parentNode
  let wtSpreader = htBorders.parentNode
  let wtBorders = htBorders.querySelectorAll('.area')
  let left = parseInt(wtBorders[1].style.left) + 1
  let height = parseInt(wtBorders[1].style.height) + 2
  let top = parseInt(wtBorders[0].style.top)
  let width = parseInt(wtBorders[0].style.width) + 2
  ele.style.left = left + 'px'
  ele.style.top = top + 'px'
  ele.setAttribute("data-target-top",top + parseInt(wtSpreader.style.top))
  ele.setAttribute("data-target-left",left + parseInt(wtSpreader.style.left))
  ele.innerHTML = `<svg id="dash_spin" width="${width}px" height="${height}px" style="pointer-events: none;"><path id="line_green" stroke="#1FBB7D" stroke-width="2" fill="none" d="m0,0 v${height} h${width} v-${height} h-${width} z"></path><path id="dash_white" stroke="#FFFFFF" stroke-width="2" fill="none" stroke-dasharray="6" stroke-dashoffset="8" d="m0,0 v${height} h${width} v-${height} h-${width} z"></path></svg>`
}

function canPaste(src, dst, mergeCells) {
  const cellRange = dst.clone()
  const [srn, scn] = src.size()
  const [drn, dcn] = dst.size()
  if (srn > drn) {
    cellRange.eri = dst.sri + srn - 1
  }
  if (scn > dcn) {
    cellRange.eci = dst.sci + scn - 1
  }
  for (let i = 0; i < mergeCells.length; i++) {
    const merge = mergeCells[i]
    const it = new CellRange(merge.row, merge.col, merge.row + merge.rowspan - 1, merge.col + merge.colspan - 1);
    if (it.intersects(cellRange)) {
      return false
    }
  }
  return true;
}

function isSameCellMerge(rowOffset, colOffset, mergeCells, context) {
  const mergeIndex = {}
  for(var k = 0; k < mergeCells.length; k++) {
    let {row, col, rowspan, colspan} = mergeCells[k]
    mergeIndex[row + ',' + col] = {rowspan, colspan}
  }
  for(var i = 0; i < copyData.data.length; i++) {
    let row = copyData.data[i]
    for(var j = 0; j < row.length; j++) {
      let cell = row[j]
      let r = cell.rowNumber - 1 + rowOffset
      let c = cell.columnNumber - 1 + colOffset
      let sourceMerge = mergeIndex[(cell.rowNumber - 1) + ',' + (cell.columnNumber-1)]
      let targetMerge = mergeIndex[r + ',' + c]
      if((!sourceMerge && !targetMerge) || (sourceMerge && targetMerge && sourceMerge.rowspan === targetMerge.rowspan && sourceMerge.colspan === targetMerge.colspan)) {

      } else {
        return false
      }
    }
  }
  return true
}
