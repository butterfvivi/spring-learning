<template>
  <div class="component-icon-btn" @click="handleMerge">
    <div class="component-icon-btn-icon">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g fill="#3D4757" fill-rule="evenodd"><path d="M6 1v1H2v11h4v1H2a1 1 0 01-1-1V2a1 1 0 011-1h4zm3 0h4a1 1 0 011 1v11a1 1 0 01-1 1H9v-1h4V2H9V1z"/><path fill-rule="nonzero" d="M6 1h1v4H6zm2 0h1v4H8z"/><path d="M3 7.5L5 6v3zm9 0L10 6v3z"/><path d="M4 7h3v1H4zm4 0h3v1H8z"/><path fill-rule="nonzero" d="M8 10h1v4H8zm-2 0h1v4H6z"/></g></svg>
    </div>
  </div>
</template>

<script>
import { undoManager, buildNewCellDef, setDirty } from '../Utils.js'

export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {

    }
  },
  mounted: function() {

  },
  methods: {
    handleMerge() {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldMergeCells = doMergeCells(context, rowIndex, colIndex, row2Index, col2Index)
      undoManager.add({
        redo: function() {
          oldMergeCells = doMergeCells(context, rowIndex, colIndex, row2Index, col2Index)
          setDirty()
        },
        undo: function() {
          hot.updateSettings({
            mergeCells: oldMergeCells
          })
          setDirty()
        }
      })
      setDirty()
    }
  }
}

function doMergeCells(context, rowIndex, colIndex, row2Index, col2Index) {
  let doMerge = true
  let doSplit = false
  const hot = context.hot
  const mergeCells = hot.getSettings().mergeCells || []
  const oldMergeCells = mergeCells.concat([])
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const td = hot.getCell(i, j)
      if (!td) {
        continue
      }
      const colSpan = td.colSpan || 1
      const rowSpan = td.rowSpan || 1
      if (colSpan > 1 || rowSpan > 1) {
        let index = 0
        doSplit = true
        doMerge = false
        while (index < mergeCells.length) {
          const mergeItem = mergeCells[index]
          const row = mergeItem.row
          const col = mergeItem.col
          if (row === i && col === j) {
            mergeCells.splice(index, 1)
            break
          }
          index++
        }
      }
    }
  }
  if (doMerge) {
    let rowSpan = row2Index - rowIndex
    let colSpan = col2Index - colIndex

    rowSpan = rowSpan ? rowSpan + 1 : 1
    colSpan = colSpan ? colSpan + 1 : 1

    const newMergeItem = {
      row: rowIndex,
      col: colIndex,
      rowspan: rowSpan,
      colspan: colSpan
    }
    mergeCells.push(newMergeItem)
  } else {
    if (doSplit) {
      for (let i = rowIndex; i <= row2Index; i++) {
        for (let j = colIndex; j <= col2Index; j++) {
          let cellDef = context.getCell(i, j)
          if (!cellDef) {
            cellDef = buildNewCellDef(i + 1, j + 1)
            context.addCell(cellDef)
          }
        }
      }
    }
  }
  hot.updateSettings({
    mergeCells
  })
  return oldMergeCells
}
</script>
