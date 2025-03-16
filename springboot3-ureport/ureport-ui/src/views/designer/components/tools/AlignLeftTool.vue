<template>
  <el-dropdown trigger="click" style="float: left;" @command="handleAlign">
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <svg v-show="align==='left'" xmlns="http://www.w3.org/2000/svg" height="16" viewBox="0 0 16 16" width="16">
            <path d="M2 13h12v1H2zm0-3h8v1H2zm0-3h12v1H2zm0-6h12v1H2zm0 3h8v1H2z" fill="#3d4757" fill-rule="evenodd" />
          </svg>
          <svg v-show="align==='center'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
            <path d="M2 13h12v1H2v-1zm2-3h8v1H4v-1zM2 7h12v1H2V7zm0-6h12v1H2V1zm2 3h8v1H4V4z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
          <svg v-show="align==='right'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
            <path d="M2 13h12v1H2v-1zm4-3h8v1H6v-1zM2 7h12v1H2V7zm0-6h12v1H2V1zm4 3h8v1H6V4z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="left">
        <svg
          style="vertical-align: text-bottom;"
          xmlns="http://www.w3.org/2000/svg"
          height="16"
          viewBox="0 0 16 16"
          width="16"
        >
          <path d="M2 13h12v1H2zm0-3h8v1H2zm0-3h12v1H2zm0-6h12v1H2zm0 3h8v1H2z" fill="#3d4757" fill-rule="evenodd" />
        </svg>
        居左对齐
      </el-dropdown-item>
      <el-dropdown-item command="center">
        <span>
          <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
            <path d="M2 13h12v1H2v-1zm2-3h8v1H4v-1zM2 7h12v1H2V7zm0-6h12v1H2V1zm2 3h8v1H4V4z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
        </span>
        居中对齐
      </el-dropdown-item>
      <el-dropdown-item command="right">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
          <path d="M2 13h12v1H2v-1zm4-3h8v1H6v-1zM2 7h12v1H2V7zm0-6h12v1H2V1zm4 3h8v1H6V4z" fill="#3D4757" fill-rule="evenodd" />
        </svg>
        居右对齐
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'

export default {
  name: 'AlignLeftTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      align: 'left'
    }
  },
  mounted: function() {

  },
  methods: {
    handleAlign(align) {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.align = align
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldAlign = buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, align)
      undoManager.add({
        redo: function() {
          oldAlign = buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, align)
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
              cellStyle.align = oldAlign[i + ',' + j]
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
      this.align = cellStyle.align || 'left'
    }
  }
}

function buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, align) {
  const oldAlign = {}
  const hot = context.hot
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldAlign[i + ',' + j] = cellStyle.align || 'left'
      cellStyle.align = align
    }
  }
  hot.render()
  return oldAlign
}
</script>

<style>
</style>
