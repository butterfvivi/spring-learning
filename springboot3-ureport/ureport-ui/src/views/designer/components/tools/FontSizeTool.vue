<template>
  <el-dropdown size="small" trigger="click" style="float: left;" @command="handleFontSize">
    <span class="el-dropdown-link">
      <div class="component-icon-btn selfdefine">
        <div class="component-icon-btn-icon" style="line-height: 16px;width: 60px;float: left;font-size: 12px;">
          {{ fontSize }}
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item v-for="(value,index) in fontSizes" :command="value">{{ value }}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'
import { fontSizes } from '@/data/select-options'

export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      fontSizes: fontSizes,
      fontSize: 10
    }
  },
  mounted: function() {

  },
  methods: {
    handleFontSize(fontSize) {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.fontSize = fontSize
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldFontSize = updateFontSize(context, rowIndex, colIndex, row2Index, col2Index, fontSize)
      undoManager.add({
        redo: function() {
          oldFontSize = updateFontSize(context, rowIndex, colIndex, row2Index, col2Index, fontSize)
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
              cellStyle.fontSize = oldFontSize[i + ',' + j] || 10
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
      const fontSize = cellStyle.fontSize || 10
      this.fontSize = fontSize
    }
  }
}

function updateFontSize(context, rowIndex, colIndex, row2Index, col2Index, fontSize) {
  const hot = context.hot
  const oldFontSize = {}
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldFontSize[i + ',' + j] = cellStyle.fontSize || 10
      cellStyle.fontSize = fontSize
    }
  }
  hot.render()
  return oldFontSize
}
</script>

<style>
</style>
