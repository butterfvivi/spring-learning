<template>
  <div class="component-icon-btn" @click="setSlash">
    <div class="component-icon-btn-icon">
      <i class="ureport ureport-crosstab" style="font-size: 15px;color: #000;"></i>
    </div>
  </div>
</template>

<script>
  import Handsontable from 'handsontable'
  import { undoManager } from '../Utils.js'

  export default {
    props: {
      context: {
        type: Object,
        required: true
      },
    },
    data: function() {
      return {

      }
    },
    mounted: function() {

    },
    methods: {
      setSlash() {
        const context = this.context
        const table = this.context.hot
        const selected = table.getSelected()
        if (selected) {
          const rowIndex = selected[0]
          const colIndex = selected[1]
          const row2Index = selected[2]
          const col2Index = selected[3]
          const cellDef = this.context.getCell(rowIndex, colIndex)
          if (!cellDef) {
            return
          }
          if (cellDef.value.type !== 'slash') {
            let oldCellDataValue = cellDef.value
            let oldExpand  = cellDef.expand
            let newCellDataValue = {
              type: 'slash'
            }
            cellDef.value = newCellDataValue
            cellDef.expand = 'None'
            this.context.hot.setDataAtCell(rowIndex, colIndex, '')
            this.context.hot.render()
            context.refreshSelected()
            undoManager.add({
              redo:function(){
                cellDef.value = newCellDataValue
                cellDef.expand = 'None'
                table.setDataAtCell(rowIndex, colIndex, '')
                context.refreshSelected()
              },
              undo:function(){
                cellDef.value = oldCellDataValue
                cellDef.expand = oldExpand
                table.setDataAtCell(rowIndex, colIndex, '')
                context.refreshSelected()
              }
            })
          }
        }
      },
    }
  }
</script>

<style>
</style>
