<template>
  <div class="component-icon-btn" style="height: 24px;">
    <div class="component-dropdown-group component-icon-btn-icon" style="float: left;" @click="handleBgColor">
      <i class="icons icons-16 icons-16-fill_color" />
      <div class="component-icon-btn-color" :style="{backgroundColor:color}" />
    </div>
    <el-dropdown ref="toolBgColorDropdown" size="mini" trigger="click" @visible-change="handleDropdownMenu">
      <span class="el-dropdown-link">
        <div class="component-dropdown-btn" style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item style="padding:0px;background:#fff">
          <div style="cursor: default;padding:10px;padding-bottom: 0px;" @click.stop="">
            <table>
              <tbody>
                <tr v-for="(arr,i) in colors" :key="i">
                  <td v-for="(color,j) in arr" :key="j" @click="changeColor(color)">
                    <div class="x-spreadsheet-color-palette-cell" :style="{backgroundColor:color}" />
                  </td>
                </tr>
              </tbody>
            </table>
            <div style="padding-bottom: 6px;margin-top: 5px;padding-top: 8px;border-top: 1px solid #dcdfe6;text-align: right;">
              <el-input v-model="custom" placeholder="rgb(0, 1, 0)" size="mini" style="width: 166px;" />
              <el-button plain size="mini" style="margin-left: 10px;margin-right: 4px;" @click="saveColor()">确 定</el-button>
            </div>
          </div>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'
import { colors } from '@/data/select-options'

export default {
  name: 'BgcolorTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      colors: colors,
      color: 'rgb(255, 255, 255)',
      custom: ''
    }
  },
  mounted: function() {

  },
  methods: {
    handleDropdownMenu(val) {
      if (val) {
        this.custom = this.color
      }
    },
    saveColor() {
      if (!this.custom) {
        this.$message('请输入颜色值')
        return
      }
      const custom = this.custom.toLocaleLowerCase().trim().replace(/' '/, '')
      let valid = true
      valid = valid && custom.startsWith('rgb(') && custom.endsWith(')')
      const arr = custom.replace('rgb(', '').replace(')', '').split(',')
      valid = valid && arr.length === 3
      for (const val of arr) {
        valid = valid && this.checkColorValue(val)
      }
      if (!valid) {
        this.$message('颜色值不合法')
        return
      }
      const color = `rgb(${parseInt(arr[0].trim())},${parseInt(arr[1].trim())},${parseInt(arr[2].trim())})`
      this.changeColor(color)
    },
    checkColorValue(val) {
      return !isNaN(val) && parseInt(val) <= 255 && parseInt(val) >= 0
    },
    changeColor(color) {
      this.color = color
      this.handleBgColor()
      this.$refs.toolBgColorDropdown.hide()
    },
    handleBgColor() {
      const context = this.context
      const hot = context.hot
      const selected = context.selectionCellRange()
      if (!selected) {
        return
      }
      const color = this.color.replace('rgb(', '').replace(')', '')
      const { rowIndex, colIndex, row2Index, col2Index } = selected
      let oldForecolorStyle = updateCellsBgcolorStyle(context, rowIndex, colIndex, row2Index, col2Index, color)
      undoManager.add({
        redo: function() {
          oldForecolorStyle = updateCellsBgcolorStyle(context, rowIndex, colIndex, row2Index, col2Index, color)
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
              const bgcolor = oldForecolorStyle[i + ',' + j]
              cellStyle.bgcolor = bgcolor
            }
          }
          setDirty()
          hot.render()
        }
      })
      setDirty()
    }
  }
}

function updateCellsBgcolorStyle(context, rowIndex, colIndex, row2Index, col2Index, bgcolor) {
  const hot = context.hot
  const oldForecolorStyle = {}
  for (let i = rowIndex; i <= row2Index; i++) {
    for (let j = colIndex; j <= col2Index; j++) {
      const cellDef = context.getCell(i, j)
      if (!cellDef) {
        continue
      }
      const cellStyle = cellDef.cellStyle
      oldForecolorStyle[i + ',' + j] = cellStyle.bgcolor || '255, 255, 255'
      cellStyle.bgcolor = bgcolor
    }
  }
  hot.render()
  return oldForecolorStyle
}
</script>

<style>
</style>
