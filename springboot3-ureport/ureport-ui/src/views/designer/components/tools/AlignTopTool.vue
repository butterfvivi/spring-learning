<template>
  <el-dropdown trigger="click" style="float: left;" @command="handleValign">
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <svg v-show="valign==='top'" xmlns="http://www.w3.org/2000/svg" width="16" height="16">
            <path d="M8 8H7v6h1zm-.5-3L10 8H5zM2 3h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
          <svg v-show="valign==='middle'" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
            <path d="M8 12H7v3h1zm-.5-3l2.5 3H5zM7 3h1V0H7zm.5 3L5 3h5zM2 7h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
          <svg v-show="valign==='bottom'" xmlns="http://www.w3.org/2000/svg" width="16" height="16">
            <path d="M7 9h1V3H7zm.5 3L5 9h5zM2 13h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
          </svg>
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="top">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16">
          <path d="M8 8H7v6h1zm-.5-3L10 8H5zM2 3h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
        </svg>
        顶部对齐
      </el-dropdown-item>
      <el-dropdown-item command="middle">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16">
          <path d="M8 12H7v3h1zm-.5-3l2.5 3H5zM7 3h1V0H7zm.5 3L5 3h5zM2 7h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
        </svg>
        中部对齐
      </el-dropdown-item>
      <el-dropdown-item command="bottom">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16">
          <path d="M7 9h1V3H7zm.5 3L5 9h5zM2 13h11v1H2z" fill="#3D4757" fill-rule="evenodd" />
        </svg>
        底部对齐
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'

export default {
  name: 'AlignTopTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      valign: 'middle'
    }
  },
  mounted() {

  },
  methods: {
    handleValign(valign) {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      this.valign = valign
      const that = this
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldAlign = buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, valign)
      undoManager.add({
        redo: function() {
          oldAlign = buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, valign)
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
              cellStyle.valign = oldAlign[i + ',' + j]
              that.valign = cellStyle.valign
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
      this.valign = cellStyle.valign || 'middle'
    }
  }
}

function buildCellAlign(context, rowIndex, colIndex, row2Index, col2Index, valign) {
  const oldAlign = {}
  const hot = context.hot
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldAlign[i + ',' + j] = cellStyle.valign || 'middle'
      cellStyle.valign = valign
    }
  }
  hot.render()
  return oldAlign
}
</script>

<style>
</style>
