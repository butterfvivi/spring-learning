<template>
  <div class="component-icon-btn" @click="handleBold">
    <div :class="bold ? 'component-icon-btn-icon selected':'component-icon-btn-icon'">
      <i class="icons icons-16 icons-16-font_bold" />
    </div>
  </div>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'

export default {
  name: 'BoldTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      bold: false
    }
  },
  mounted: function() {

  },
  methods: {
    handleBold() {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.bold = !this.bold
      const bold = this.bold
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldBoldStyle = updateCellsBoldStyle(context, rowIndex, colIndex, row2Index, col2Index, bold)
      undoManager.add({
        redo: function() {
          oldBoldStyle = updateCellsBoldStyle(context, rowIndex, colIndex, row2Index, col2Index, bold)
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
              const bold = oldBoldStyle[i + ',' + j]
              cellStyle.bold = bold
            }
          }
          setDirty()
          that.refresh()
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
      this.bold = !!cellStyle.bold
    }
  }
}

function updateCellsBoldStyle(context, rowIndex, colIndex, row2Index, col2Index, bold) {
  const hot = context.hot
  const oldBoldStyle = {}
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldBoldStyle[i + ',' + j] = cellStyle.bold
      cellStyle.bold = bold
    }
  }
  hot.render()
  return oldBoldStyle
}
</script>

<style>
</style>
