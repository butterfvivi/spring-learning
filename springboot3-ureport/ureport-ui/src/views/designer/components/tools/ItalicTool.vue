<template>
  <div class="component-icon-btn" @click="handleItalic">
    <div :class="italic ? 'component-icon-btn-icon selected':'component-icon-btn-icon'">
      <i class="icons icons-16 icons-16-font_italic" />
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
      italic: false
    }
  },
  mounted: function() {

  },
  methods: {
    handleItalic() {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.italic = !this.italic
      const italic = this.italic
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldItalicStyle = updateCellsItalicStyle(context, rowIndex, colIndex, row2Index, col2Index, italic)
      undoManager.add({
        redo: function() {
          oldItalicStyle = updateCellsItalicStyle(context, rowIndex, colIndex, row2Index, col2Index, italic)
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
              const italic = oldItalicStyle[i + ',' + j]
              cellStyle.italic = italic
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
      this.italic = !!cellStyle.italic
    }
  }
}

function updateCellsItalicStyle(context, startRow, startCol, endRow, endCol, italic) {
  const hot = context.hot
  const oldItalicStyle = {}
  for (let i = startRow; i <= endRow; i++) {
    for (let j = startCol; j <= endCol; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldItalicStyle[i + ',' + j] = cellStyle.italic
      cellStyle.italic = italic
    }
  }
  hot.render()
  return oldItalicStyle
}
</script>
