<template>
  <el-dropdown
    trigger="click"
    style="float: left;"
    @command="handleOpen"
  >
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <i class="icons icons-16 icons-16-picture" />
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="float-image">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g transform="translate(1 1)" fill="none" fill-rule="evenodd"><path d="M1 .5a.5.5 0 00-.5.5v11a.5.5 0 00.5.5h12a.5.5 0 00.5-.5V1a.5.5 0 00-.5-.5H1zM.5.5h13v12H.5V.5z" stroke="#3D4757" /><circle fill="#3D4757" fill-rule="nonzero" cx="10" cy="4" r="1" /><path d="M8.473 11.223L4.47 7.107 1 10.667v-1.5C2.715 7.6 3.707 6.664 3.975 6.358c.402-.457.651-.39 1.042 0L8.473 10l2.486-2.651c.414-.462.62-.462 1.011-.071L14 10.06v1.5l-2.51-3.41-3.017 3.072z" fill="#3D4757" /></g></svg>
        浮动图片
      </el-dropdown-item>
      <el-dropdown-item command="cell-image">
        <svg style="vertical-align: text-bottom;" xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g fill="none" fill-rule="evenodd"><rect stroke="#3D4757" x="1.5" y="1.5" width="13" height="13" rx="1" /><path fill="#3D4757" d="M2 5h12v1H2z" /><path d="M14.7 11.814l-2.601-2.48a.5.5 0 00-.697.007l-2.367 2.34-1.698-1.62a.5.5 0 00-.724.036L5.3 11.626l.629.703 1.087-1.265L9.046 13l2.712-2.681 2.378 2.267.564-.772zM9 9a1 1 0 100-2 1 1 0 000 2z" fill="#3D4757" fill-rule="nonzero" /><path fill="#3D4757" d="M6 2v13H5V2z" /></g></svg>
        单元格图片
      </el-dropdown-item>
    </el-dropdown-menu>
    <el-dialog
      title="浮动图片"
      :visible.sync="dialogVisible"
      append-to-body
      :close-on-click-modal="false"
      width="600px"
    >
      <el-form class="div-block-clearance" label-position="left" :model="form" size="mini" label-width="90px" style="height: 240px;">
        <el-form-item label="">
          <el-checkbox-group v-model="form.type">
            <el-checkbox :label="true">只展示第一页图片</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="图片地址:">
          <el-input v-model="form.path" type="textarea" :rows="8" />
        </el-form-item>
        <el-form-item label="">
          <el-upload
            action="#"
            :show-file-list="false"
            :accept="'.png,.jpg'"
            :http-request="uploadFn"
            :on-change="getFile"
          >
            <el-button slot="trigger">选择本地图片</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="handleFloatImage">确 定</el-button>
      </span>
    </el-dialog>
  </el-dropdown>
</template>
<script>

import { undoManager } from '../Utils.js'

export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      dialogVisible: false,
      imageUrl: '',
      sourceOptions: [
        { label: 'URL', value: 'text' },
        { label: '上传图片', value: 'upload' }
      ],
      form: {
        type: false,
        source: 'text',
        path: ''
      }
    }
  },
  mounted: function() {
    window.removeFloatImage = this.removeFloatImage
  },
  methods: {
    getFile(file, fileList) {
      this.getBase64(file.raw).then(res => {
        this.form.path = res
        //this.cellDef.value.value = res
      })
    },
    getBase64(file) {
      return new Promise(function(resolve, reject) {
        const reader = new FileReader()
        let imgResult = ''
        reader.readAsDataURL(file)
        reader.onload = function() {
          imgResult = reader.result
        }
        reader.onerror = function(error) {
          reject(error)
        }
        reader.onloadend = function() {
          resolve(imgResult)
        }
      })
    },
    uploadFn() {

    },
    handleOpen(command) {
      if (command === 'float-image') {
        this.dialogVisible = true
        // this.handleFloatImage()
      } else if (command === 'cell-image') {
        this.handleCellImage()
      }
    },
    removeFloatImage(ele) {
      const parentElement = ele.parentNode
      parentElement.parentNode.removeChild(parentElement)
    },
    handleFloatImage() {
      let value = ''
      if (this.form.source === 'text') {
        if (!this.form.path) {
          this.$message('请选择图片')
          return
        }
        value = this.form.path
      }
      if (this.form.source === 'upload') {
        if (!this.imageUrl) {
          this.$message('请选择图片')
          return
        }
        value = this.imageUrl
      }
      this.context.floatImage.handleFloatImage({
        type: this.form.type,
        source: 'text',
        value: value,
        width: 200
      })
      this.dialogVisible = false
    },
    handleCellImage() {
      const context = this.context
      const hot = this.context.hot
      const selected = hot.getSelected()
      if (selected) {
        const rowIndex = selected[0]
        const colIndex = selected[1]
        const row2Index = selected[2]
        const col2Index = selected[3]
        const cellDef = this.context.getCell(rowIndex, colIndex)
        if (!cellDef) {
          return
        }
        if (cellDef.value.type !== 'image') {
          const oldCellDataValue = cellDef.value
          const oldExpand = cellDef.expand
          const td = this.context.hot.getCell(rowIndex, colIndex)
          const width = this._buildWidth(colIndex, td.colSpan, this.context.hot)
          const height = this._buildHeight(rowIndex, td.rowSpan, this.context.hot)
          const newCellDataValue = {
            width,
            height,
            type: 'image',
            source: 'text'
          }
          cellDef.value = newCellDataValue
          cellDef.expand = 'None'
          this.context.hot.setDataAtCell(rowIndex, colIndex, '')
          this.context.hot.render()
          context.refreshSelected()

          undoManager.add({
            redo: function() {
              cellDef.value = newCellDataValue
              cellDef.expand = 'None'
              hot.setDataAtCell(rowIndex, colIndex, '')
              context.refreshSelected()
            },
            undo: function() {
              cellDef.value = oldCellDataValue
              cellDef.expand = oldExpand
              hot.setDataAtCell(rowIndex, colIndex, '')
              context.refreshSelected()
            }
          })
        }
      }
    },
    _buildWidth(colIndex, colspan, hot) {
      let width = hot.getColWidth(colIndex) - 3
      if (!colspan || colspan < 2) {
        return width
      }
      const start = colIndex + 1
      const end = colIndex + colspan
      for (let i = start; i < end; i++) {
        width += hot.getColWidth(i)
      }
      return width
    },
    _buildHeight(rowIndex, rowspan, hot) {
      let height = hot.getRowHeight(rowIndex) - 3
      if (!rowspan || rowspan < 2) {
        return height
      }
      const start = rowIndex + 1
      const end = rowIndex + rowspan
      for (let i = start; i < end; i++) {
        height += hot.getRowHeight(i)
      }
      return height
    }
  }
}
</script>