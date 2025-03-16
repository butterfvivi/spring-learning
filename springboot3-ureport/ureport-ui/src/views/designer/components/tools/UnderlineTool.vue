<template>
  <div class="component-icon-btn" @click="handleUnderline">
    <div :class="underline ? 'component-icon-btn-icon selected':'component-icon-btn-icon'">
      <i class="icons icons-16 icons-16-font_underline"></i>
    </div>
  </div>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'

export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      underline: false
    }
  },
  mounted: function() {

  },
  methods: {
    handleUnderline() {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.underline = !this.underline
      const underline = this.underline
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldUnderlineStyle = updateCellsUnderlineStyle(context, rowIndex, colIndex, row2Index, col2Index, underline)
      undoManager.add({
        redo: function() {
          oldUnderlineStyle = updateCellsUnderlineStyle(context, rowIndex, colIndex, row2Index, col2Index, underline)
          that.refresh()
          setDirty()
        },
        undo: function() {
          for (let i = rowIndex; i <= row2Index; i++) {
            for (let j = colIndex; j <= col2Index; j++) {
              const cellDef = context.getCell(i, j)
              if (!cellDef) {
                continue
              }
              const cellStyle = cellDef.cellStyle
              let underline = oldUnderlineStyle[i + ',' + j]
              cellStyle.underline = underline
            }
          }
          that.refresh()
          setDirty()
          hot.render()
        }
      })
      setDirty()
    },
    refresh() {
      const cellDef = this.context.getSelectedCell()
      if (!cellDef) {
        return
      }
      const cellStyle = cellDef.cellStyle
      this.underline = cellStyle.underline || false
    }
  }
}

function updateCellsUnderlineStyle(context, startRow, startCol, endRow, endCol, underline) {
  const oldUnderlineStyle = {}
  const hot = context.hot
  for (let i = startRow; i <= endRow; i++) {
    for (let j = startCol; j <= endCol; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldUnderlineStyle[i + ',' + j] = cellStyle.underline
      cellStyle.underline = underline
    }
  }
  hot.render()
  return oldUnderlineStyle
}
</script>
