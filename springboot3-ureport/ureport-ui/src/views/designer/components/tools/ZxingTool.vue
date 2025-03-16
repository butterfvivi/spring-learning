<template>
  <el-dropdown
  trigger="click"
  @command="handleCommand"
  style="float: left;">
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><g fill="none" fill-rule="evenodd"><path stroke="#3D4757" d="M1.5 1.5h5v5h-5zm0 7h5v5h-5zm7-7h5v5h-5z"/><path fill="#3D4757" d="M13 11h1v3h-1zM8 9h1v2H8zm4 0h1v1h-1zm-4 3h1v1H8zm2-1h1v1h-1zM8 8h3v1H8zm4 0h2v1h-2zm-4 5h2v1H8z"/><path fill="#3D4757" d="M12 13h2v1h-2zm-1-3h1v1h-1zm-1-1h1v1h-1zm0 3h2v1h-2zM3 3h2v2H3zm0 7h2v2H3zm7-7h2v2h-2z"/></g></svg>
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn"></div>
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="qrcode">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><g fill="none" fill-rule="evenodd"><path stroke="#3D4757" d="M1.5 1.5h5v5h-5zm0 7h5v5h-5zm7-7h5v5h-5z"/><path fill="#3D4757" d="M13 11h1v3h-1zM8 9h1v2H8zm4 0h1v1h-1zm-4 3h1v1H8zm2-1h1v1h-1zM8 8h3v1H8zm4 0h2v1h-2zm-4 5h2v1H8z"/><path fill="#3D4757" d="M12 13h2v1h-2zm-1-3h1v1h-1zm-1-1h1v1h-1zm0 3h2v1h-2zM3 3h2v2H3zm0 7h2v2H3zm7-7h2v2h-2z"/></g></svg>
        二维码
      </el-dropdown-item>
      <el-dropdown-item command="barcode">
        <svg class="icon" style="vertical-align: text-bottom;height: 16px;fill: currentColor;overflow: hidden;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4151"><path d="M444.416 192.512h38.912v541.696h-38.912V192.512zM347.648 192.512h57.856v541.696H347.648V192.512zM308.736 192.512h19.456v541.696h-19.456V192.512zM173.568 192.512h19.456v541.696h-19.456V192.512zM211.968 192.512h57.856v541.696H211.968V192.512zM753.664 192.512h57.856v541.696h-57.856V192.512zM637.952 192.512h57.856v541.696h-57.856V192.512zM715.264 192.512h19.456v541.696h-19.456V192.512zM637.952 773.12h212.992v57.856h-212.992V773.12zM76.8 192.512h57.856v638.464H76.8V192.512zM541.184 773.12H599.04v57.856h-57.856V773.12zM831.488 192.512h19.456v541.696h-19.456V192.512zM889.344 192.512H947.2v638.464h-57.856V192.512zM579.584 192.512h38.912v541.696h-38.912V192.512zM173.568 773.12h57.856v57.856H173.568V773.12zM270.336 773.12h232.448v57.856H270.336V773.12zM521.728 192.512h19.456v541.696h-19.456V192.512z" fill="#333333" p-id="4152"></path></svg>
        条形码
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
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

    }
  },
  mounted: function() {

  },
  methods: {
    handleCommand(val) {
      const context = this.context
      const hot = context.hot
      const selected = hot.getSelected()
      if (!selected) {
        return
      }
      const that = this
      const rowIndex = selected[0]
      const colIndex = selected[1]
      const row2Index = selected[2]
      const col2Index = selected[3]
      let cellDef = context.getCell(rowIndex, colIndex)
      if (!cellDef) {
        return
      }
      let oldCellDef = buildSlashes(context, rowIndex, colIndex, cellDef, val)
      undoManager.add({
        redo:function(){
          cellDef = context.getCell(rowIndex, colIndex)
          oldCellDef = buildSlashes(context, rowIndex, colIndex, cellDef, val)
          setDirty()
          that.refresh()
        },
        undo:function(){
          cellDef.value = oldCellDef.value
          cellDef.expand = oldCellDef.expand
          hot.setDataAtCell(rowIndex, colIndex, '')
          setDirty()
          hot.render()
          that.refresh()
        }
      })
      setDirty()
      that.refresh()
    },
    refresh() {
      const cellDef = this.context.refreshSelected()
    }
  }
}

function buildSlashes(context, rowIndex, colIndex, cellDef, val) {
  const hot = context.hot
  let oldCellDef = {
    value: cellDef.value,
    expand: cellDef.expand
  }
  let width = buildWidth(hot, rowIndex, colIndex)
  let height = buildHeight(hot, rowIndex, colIndex)
  if(val === 'qrcode') {
    cellDef.value = {
      width,
      height,
      type: 'zxing',
      source: 'text',
      category: 'qrcode',
      value: ''
    }
  } else if(val === 'barcode') {
    cellDef.value = {
      width,
      height,
      type:'zxing',
      category:'barcode',
      source:'text',
      format:'CODE_128',
      value:''
    }
  }
  cellDef.expand = 'None'
  hot.setDataAtCell(rowIndex, colIndex, '')
  hot.render()
  return oldCellDef
}

function buildWidth(hot, rowIndex, colIndex) {
  let td = hot.getCell(rowIndex,colIndex)
  let width = hot.getColWidth(colIndex) - 3
  let colspan = td.colSpan
  if(!colspan || colspan < 2){
    return width
  }
  let start = colIndex + 1
  let end = colIndex + colspan
  for(let i = start; i < end; i++){
    width += hot.getColWidth(i)
  }
  return width
}

function buildHeight(hot, rowIndex, colIndex) {
  let td = hot.getCell(rowIndex,colIndex)
  let height = hot.getRowHeight(rowIndex) - 3
  let rowspan = td.rowSpan
  if(!rowspan || rowspan < 2){
    return height
  }
  let start = rowIndex+1
  let end = rowIndex + rowspan
  for(let i = start; i < end; i++){
    height += hot.getRowHeight(i)
  }
  return height
}
</script>
