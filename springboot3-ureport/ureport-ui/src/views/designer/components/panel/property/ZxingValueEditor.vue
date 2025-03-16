<template>
  <el-form class="div-block-clearance" label-position="left" :model="form" size="mini" label-width="90px">
    <el-form-item label="宽:">
      <el-input v-model="form.width" oninput="value=value.replace(/[^\d]/g,'')" size="mini" style="width:100%" @change="handleWidth" />
    </el-form-item>
    <el-form-item label="高:">
      <el-input
        v-model="form.height"
        oninput="value=value.replace(/[^\d]/g,'')"
        size="mini"
        style="width:100%"
        @change="handleHeight"
      />
    </el-form-item>
    <el-form-item v-if="cellDef && cellDef.value.category==='barcode'" label="条码格式:">
      <el-select v-model="form.format" placeholder="请选择" style="width:100%" @change="handleFormat">
        <el-option v-for="code in codeOptions" :key="code" :label="code" :value="code" />
      </el-select>
    </el-form-item>
    <el-form-item label="数据来源:">
      <el-select v-model="form.source" placeholder="请选择" style="width:100%" @change="handleSource">
        <el-option v-for="item in sourceOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item v-if="form.source==='text'" label="文本:">
      <el-input v-model="form.path" @change="handlePath" />
    </el-form-item>
    <el-form-item v-if="form.source==='expression'" label="扩展方向:">
      <el-radio-group v-model="form.expand" size="mini" @change="handleExpand">
        <el-radio-button v-for="(item,index) in expands" :label="item.value">{{item.label}}</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item v-if="form.source==='expression'" label="表达式" />
    <div :style="{visibility: form.source==='expression' ? 'visible' : 'hidden'}">
      <textarea ref="zxingExpressionCode" />
    </div>
  </el-form>
</template>

<script>
import CodeMirror from 'codemirror'
import { scriptValidation } from '@/api/designer'
import { expands } from '@/data/select-options'
import { undoManager, setDirty } from '../../Utils.js'
import { refreshSelectionCellBoard } from '../../table/CellRenderer'

export default {
  props: {
    qrcodeValue: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      isInitData: false,
      expands: expands,
      cellDef: null,
      sourceOptions: [
        { label: '文本', value: 'text' },
        { label: '表达式', value: 'expression' }
      ],
      codeOptions: [
        'AZTEC',
        'CODABAR',
        'CODE_39',
        'CODE_93',
        'CODE_128',
        'DATA_MATRIX',
        'EAN_8',
        'EAN_13',
        'ITF',
        'PDF_417',
        'UPC_E',
        'UPC_A'
      ],
      form: {
        width: 0,
        height: 0,
        source: 'text',
        format: '',
        path: '',
        expand: 'None',
        expression: ''
      }
    }
  },
  beforeDestroy() {
    this.editor?.toTextArea()
  },
  watch: {
    qrcodeValue: function(val) {
      this.init(val)
    }
  },
  mounted: function() {
    this.init(this.qrcodeValue)
  },
  methods: {
    init(val) {
      if (val.context) {
        this.context = val.context
        this.refresh(val.rowIndex, val.colIndex, val.row2Index, val.col2Index)
        this.initEditor()
      }
    },
    refresh(rowIndex, colIndex, row2Index, col2Index) {
      const cellDef = this.context.getCell(rowIndex, colIndex)
      if (!cellDef) {
        return
      }
      this.rowIndex = rowIndex
      this.colIndex = colIndex
      this.row2Index = row2Index
      this.col2Index = col2Index
      this.cellDef = cellDef

      this.form.width = cellDef.value.width || 0
      this.form.height = cellDef.value.height || 0
      this.form.format = cellDef.value.format || ''
      this.form.source = cellDef.value.source || 'text'
      this.form.path = this.cellDef.value.value || ''
      this.form.expand = cellDef.expand || 'None'
    },
    initEditor() {
      const that = this
      this.$nextTick(() => {
        if (!this.editor) {
          this.editor = CodeMirror.fromTextArea(this.$refs.zxingExpressionCode, {
            mode: 'javascript',
            lineNumbers: true,
            gutters: ['CodeMirror-linenumbers', 'CodeMirror-lint-markers'],
            lint: {
              getAnnotations: buildScriptLintFunction(),
              async: true
            }
          })
          this.editor.setSize('100%', '120px')
          this.editor.on('change', function(cm, changes) {
            if(!that.isInitData) {
              const expr = cm.getValue()
              const context = that.context
              const cellDef = that.cellDef
              undo(context, cellDef, expr, 'value')
              cellDef.value.value = expr
              const td = context.hot.getCell(that.rowIndex, that.colIndex)
              refreshSelectionCellBoard(td, cellDef)
            }
          })
        }
        this.isInitData = true
        if (this.cellDef.value.source === 'expression' && this.cellDef.value.value) {
          this.editor.setValue(this.cellDef.value.value)
        } else {
          this.editor.setValue('')
        }
        this.isInitData = false
      })
    },
    handleWidth() {
      const context = this.context
      const cellDef = this.cellDef
      let width = this.form.width
      undo(context, cellDef, width, 'width')
      cellDef.value.width = width
      context.hot.render()
      setDirty()
    },
    handleHeight() {
      const context = this.context
      const cellDef = this.cellDef
      let height = this.form.height
      undo(context, cellDef, height, 'height')
      cellDef.value.height = height
      context.hot.render()
      setDirty()
    },
    handleFormat() {
      const context = this.context
      const cellDef = this.cellDef
      let format = this.form.format
      undo(context, cellDef, format, 'format')
      cellDef.value.format = format
      context.hot.render()
      setDirty()
    },
    handleSource() {
      const context = this.context
      const hot = context.hot
      const cellDef = this.cellDef

      let newSource = this.form.source
      let oldSource = cellDef.value.source
      cellDef.value.source = newSource

      let oldValue = cellDef.value.value
      cellDef.value.value = null

      let oldPath = cellDef.value.path
      this.form.path = null

      undoManager.add({
        redo:function(){
          cellDef.value.source = newSource
          cellDef.value.value = null
          cellDef.value.path = null
          hot.render()
          context.refreshSelected()
          setDirty()
        },
        undo:function(){
          cellDef.value.source = oldSource
          cellDef.value.value = oldValue
          cellDef.value.path = oldPath
          hot.render()
          context.refreshSelected()
          setDirty()
        }
      })
      context.refreshSelected()
      setDirty()
    },
    handlePath() {
      const context = this.context
      const cellDef = this.cellDef
      let path = this.form.path
      undo(context, cellDef, path, 'value')
      cellDef.value.value = path
      context.refreshSelected()
      setDirty()
    },
    handleExpand(expand) {
      const context = this.context
      const hot = context.hot
      const cellDef = this.cellDef
      let oldValue = cellDef.expand
      cellDef.expand = expand
      const td = context.hot.getCell(this.rowIndex, this.colIndex)
      refreshSelectionCellBoard(td, cellDef)
      setDirty()
      undoManager.add({
        redo:function(){
          cellDef.expand = expand
          hot.render()
          context.refreshSelected()
          setDirty()
        },
        undo:function(){
          cellDef.expand = oldValue
          hot.render()
          context.refreshSelected()
          setDirty()
        }
      })
    }
  }
}

function undo(context, cellDef, newValue, field) {
  const hot = context.hot
  let oldValue = cellDef.value[field]
  undoManager.add({
    redo:function(){
      cellDef.value[field] = newValue
      hot.render()
      context.refreshSelected()
      setDirty()
    },
    undo:function(){
      cellDef.value[field] = oldValue
      hot.render()
      context.refreshSelected()
      setDirty()
    }
  })
}

function buildScriptLintFunction() {
  return function(text, updateLinting, options, editor) {
    if (text === '') {
      updateLinting(editor, [])
      return
    }
    if (!text || text === '') {
      return
    }
    scriptValidation({
      content: text
    }).then(response => {
      const result = response.data
      if (result) {
        for (const item of result) {
          item.from = {
            line: item.line - 1
          }
          item.to = {
            line: item.line - 1
          }
        }
        updateLinting(editor, result)
      } else {
        updateLinting(editor, [])
      }
    })
  }
}
</script>
