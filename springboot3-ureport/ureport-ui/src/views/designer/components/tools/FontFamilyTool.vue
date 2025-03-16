<template>
  <el-dropdown trigger="click" style="float: left;" @command="handleFontFamily">
    <span class="el-dropdown-link">
      <div class="component-icon-btn selfdefine">
        <div class="component-icon-btn-icon" style="line-height: 16px;width: 120px;float: left;font-size: 12px;">
          {{ fontFamily }}
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="(item,index) in fontFamilys" :command="item.value">{{ item.label }}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>
<script>
import { undoManager, setDirty } from '../Utils.js'
import { fontFamilys } from '@/data/select-options'

export default {
  name: 'FontFamilyTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      fontFamilys: fontFamilys,
      show: false,
      fontFamily: '宋体'
    }
  },
  methods: {
    handleFontFamily(fontFamily) {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.fontFamily = fontFamily
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldFontFamily = updateFontFamily(context, rowIndex, colIndex, row2Index, col2Index, fontFamily)
      undoManager.add({
        redo: function() {
          oldFontFamily = updateFontFamily(context, rowIndex, colIndex, row2Index, col2Index, fontFamily)
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
              cellStyle.fontFamily = oldFontFamily[i + ',' + j]
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
      const fontFamily = cellStyle.fontFamily || '宋体'
      this.fontFamily = fontFamily
    }
  }
}

function updateFontFamily(context, startRow, startCol, endRow, endCol, fontFamily) {
  const hot = context.hot
  const oldFontFamily = {}
  for (let i = startRow; i <= endRow; i++) {
    for (let j = startCol; j <= endCol; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldFontFamily[i + ',' + j] = cellStyle.fontFamily || '宋体'
      cellStyle.fontFamily = fontFamily
    }
  }
  hot.render()
  return oldFontFamily
}
</script>
