<template>
  <el-container id="container"  v-loading="loading" style="position: absolute;overflow: hidden;background-color: #fff;">
    <el-header <el-header style="min-height: 72px;height: auto;">
      <tool-bar v-if="context" ref="childToolbar" :context="context" :tools="tools" />
      <div style="border-left: 1px solid #eeeeee;font-size: 14px;line-height: 28px;padding: 0px 10px;height: 36px;background: #f2f4f7;display: flex;">
        <div style="text-align: center;width: 120px;border: 1px solid #e2e6ed;height: 28px;background: #fff;border-radius: 4px;">{{ cellName }}</div>
        <i class="icons icons-16 icons-16-func" style="margin: auto 5px;" />
        <div id="selectionCellBoard" style="overflow:hidden;border: 1px solid #e2e6ed;height: 28px;padding-left:10px;flex:1;background: #fff;border-radius: 4px;">{{ cellContent }}</div>
      </div>
    </el-header>
    <el-main>
      <div id="designer" style="font-size: 13px;" />
    </el-main>
    <el-dialog
      id="toolDialog"
      class="tool-dialog"
      :modal="false"
      :show-close="false"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      width="420px"
      style="height: 1200px;"
    >
      <div slot="title">
        <div style="font-size: 12px;color: #606266;">当前单元格：{{cellName || '无'}}</div>
      </div>
      <el-tabs tab-position="left" style="background: #f2f4f7;height: 600px;">
        <el-tab-pane>
          <span slot="label" title="单元格设置"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g fill="none" fill-rule="evenodd"><path d="M2 1h12a1 1 0 011 1v11a1 1 0 01-1 1H2a1 1 0 01-1-1V2a1 1 0 011-1zm0 1v11h12V2H2z" fill="#3D4757" fill-rule="nonzero" /><path fill="#909AA9" opacity=".559" d="M6 2h4v11H6z" /><path fill="#3D4757" fill-rule="nonzero" d="M2 5h12v1H2zm0 4h12v1H2z" /><path fill="#3D4757" fill-rule="nonzero" d="M5 1.962h1V14H5zm5 0h1V14h-1z" /></g></svg></span>
          <property-panel v-if="context" :context="context" ref="childPropertyPanel"  />
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label" title="数据集设置"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"><g fill="#3D4757" fill-rule="evenodd"><path d="M3 2h10a1 1 0 011 1v11a1 1 0 01-1 1H3a1 1 0 01-1-1V3a1 1 0 011-1zm0 1v11h10V3H3z" /><path d="M4 4h3v1H4zm0 4h3v1H4zm0 4h3v1H4zM3 6h10v1H3zm0 4h10v1H3z" /></g></svg></span>
          <data-set :data-set-value="dataSetValue" :refresh-child-property-panel="refreshChildPropertyPanel" style="height: 100%;flex: 1;" />
        </el-tab-pane>
        <el-tab-pane>
          <span slot="label" title="单元格链接" style="font-size: 16px;"><i class="el-icon-link" /></span>
          <url-property ref="urlPropertyPanel" :cell-def="cellDef" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </el-container>
</template>

<script>
import 'codemirror/lib/codemirror.css'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/lint/lint.css'

import 'handsontable/dist/handsontable.css'
import Context from './components/Context.js'
import ReportTable from './components/table/ReportTable.js'
import PropertyPanel from './components/panel/PropertyPanel'

import DialogDraggable from './components/draggable.js'

import DataSet from './components/dataset'
import { undoManager, debounce } from './components/Utils.js'
import DragField from './components/DragField.js'
import PrintLine from './components/PrintLine.js'
import FreezeLine from './components/FreezeLine.js'
import { renderRowHeader } from './components/table/HeaderUtils.js'
import ToolBar from './components/tools/ToolBar'
import UrlProperty from './components/tools/components/UrlProperty'
import { refreshSelectionCellBoard } from './components/table/CellRenderer.js'
import FloatImage from './components/FloatImage.js'

export default {
  name: 'Table',
  components: {
    ToolBar,
    UrlProperty,
    PropertyPanel,
    DataSet
  },
  data: function() {
    return {
      dialogVisible: true,
      loading: true,
      cellName: '',
      cellContent: '',
      reportFullName: '未命名',
      extend: false,
      activeName: 'first',
      context: null,
      tools: [],
      dataSetValue: {},
      handsontableInst: null,
      cellDef: {}
    }
  },
  watch: {
    $route(to, from) {
      if (to.fullPath.startsWith("/ureport/designer")) {
        if(this.context) {
          this.context.reportTable.loadFile(this.$route.params.reportName)
        } else {
          this.init()
        }
      }
    }
  },
  mounted: function() {
    this.init()
  },
  destroyed: function() {
    // 销毁Handsontable实例
    this.handsontableInst?.destroy()
  },
  methods: {
    init: function() {
      let reportName = this.$route.params.reportName || ''
      const elementId = 'designer'
      const ele = document.getElementById(elementId)
      const that = this
      this.$nextTick(function() {
        const el = document.getElementById('toolDialog')
        DialogDraggable(el)
      })
      undoManager.setLimit(100)
      const _this = this
      const reportTable = new ReportTable(reportName, ele, function() {
        _this.context = new Context(this)
        _this.context.elementId = elementId
        _this.createCopyBorderDiv(ele)
        _this.createFloatImageDiv(ele)
        _this.bindScroll(ele)
        this.bindSelectionEvent(function(rowIndex, colIndex, row2Index, col2Index) {
          _this.correctionHandsontableInputHolder(ele, rowIndex, colIndex)
          _this.context.setSelection(rowIndex, colIndex, row2Index, col2Index)
          const td = _this.context.hot.getCell(rowIndex, colIndex)
          const cell = _this.context.getCell(rowIndex, colIndex)
          refreshSelectionCellBoard(td, cell)
          _this.cellDef = cell
          _this.cellName = _this.context.getCellName(rowIndex, colIndex)
          _this.$refs.childPropertyPanel.refresh(rowIndex, colIndex, row2Index, col2Index)
          _this.$refs.childToolbar.refresh(rowIndex, colIndex, row2Index, col2Index)
        })
        _this.printLine = new PrintLine(_this.context)
        _this.freezeLine = new FreezeLine(_this.context)
        _this.floatImage = new FloatImage(_this.context)

        const floatImages = _this.context.reportDef.floatImages || []
        for(let i = 0; i < floatImages.length; i++) {
          let f = floatImages[i]
          _this.floatImage.handleFloatImage({
            type: f.onlyFirstPage,
            source: 'text',
            value: f.value,
            height: f.height,
            width: f.width,
            left: f.left,
            top: f.top
          })
        }
        const rows = _this.context.reportDef.rows
        for (const row of rows) {
          const band = row.band
          if (!band) {
            continue
          }
          _this.context.addRowHeader(row.rowNumber - 1, band)
        }
        renderRowHeader(_this.context.hot, _this.context)
        _this.dataSetValue = {
          context: _this.context
        }
        _this.loading = false
      })
      this.handsontableInst = reportTable.hot
      DragField.call(this)
    },
    resizeTable(offsetLeft, offsetRight) {
      if (this.context && this.context.hot) {
        const height = document.documentElement.clientHeight
        const width = document.documentElement.clientWidth
        const ele = document.getElementById('designerPanel')

        let offset = ele.getBoundingClientRect().left
        if (offsetLeft || offsetRight) {
          offset = offset + offsetLeft + offsetRight
        }
        this.context.hot.updateSettings({
          height: height - ele.getBoundingClientRect().top - 36,
          width: width - offset
        })
        this.context.printLine.refresh()
        this.context.freezeLine.refresh()
      }
    },
    refreshChildPropertyPanel(rowIndex, colIndex, row2Index, col2Index) {
      this.$refs.childPropertyPanel.refresh(rowIndex, colIndex, row2Index, col2Index)
    },
    bindScroll(ele) {
      const targets = ele.querySelectorAll('.wtHolder')
      targets[0].addEventListener('scroll', function() {
        debounce(() => {
          const wtHider = this.children[0]
          const wtSpreader = wtHider.children[0]
          const left = parseInt(wtSpreader.style.left)
          const top = parseInt(wtSpreader.style.top)
          const copyBorderBlock = document.getElementById('copy_border_block')
          copyBorderBlock.style.left = parseInt(copyBorderBlock.getAttribute('data-target-left')) - left + 'px'
          copyBorderBlock.style.top = parseInt(copyBorderBlock.getAttribute('data-target-top')) - top + 'px'
        }, 50)
      })
    },
    createCopyBorderDiv(ele) {
      const targets = ele.querySelectorAll('.htBorders')
      var child = document.createElement('div')
      child.id = 'copy_border_block'
      targets[0].appendChild(child)
    },
    createFloatImageDiv(ele) {
      const targets = ele.querySelectorAll('.wtHolder')
      var child = document.createElement('div')
      child.id = 'float_image_block'
      targets[0].appendChild(child)
    },
    correctionHandsontableInputHolder(ele, rowIndex, colIndex) {
      const targets = ele.querySelectorAll('.handsontableInputHolder')
      const target = targets[targets.length - 1]
      target.style.marginTop = '0px'
      if (rowIndex > 0) {
        target.style.marginTop = '1px'
      }
      target.style.marginLeft = '0px'
      if (colIndex > 0) {
        target.style.marginLeft = '1px'
      }
    }
  }
}
</script>

<style>
  body {
    -webkit-user-select:none;
    -moz-user-select:none;
    -ms-user-select:none;
    user-select:none;
    overflow: hidden;
  }

  div.cell {
    -webkit-user-select:text;
    -moz-user-select:text;
    -ms-user-select:text;
    user-select:text;
  }

  img {
    vertical-align: middle;
    border: 0;
  }

  .tool-top-btn {
    margin-top: 10px;
    font-size: 12px;
    margin-left: 20px;
  }

  .dataset-item {
    display: none
  }

  .el-tree-node__content:hover .dataset-item {
    display: inline-block;
  }

  .handsontable .htDimmed {
    color: rgb(0,1,0);
  }

  .handsontable tr,.handsontable td,.handsontable th {
    background: transparent;
  }

  .handsontable table.htCore {
    border-collapse: collapse
  }

  .handsontable th {
    border-color: #eeeff1 !important;
    background: #f4f5f8;
    border-bottom: 1px solid #eeeff1;
    color: #909399;
  }

  .handsontable tr:first-child th,
  .handsontable tr:first-child td {
    border-top: 1px solid #e6e6e6;
  }

  .handsontable th, .handsontable td {
    border-top: 1px solid #e6e6e6;
    border-right: 1px solid #e6e6e6;
    border-bottom: 1px solid #e6e6e6;
    border-left: 1px solid #e6e6e6;
  }

  .handsontable tbody th.ht__highlight,
  .handsontable thead th.ht__highlight {
    background-color: #e7edf9;
  }

  .handsontable .wtBorder.current.corner {
    width: 4px !important;
    height: 4px !important;
    cursor: default;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    -webkit-pointer-events: none;
    -moz-pointer-events: none;
    -ms-pointer-events: none;
    pointer-events: none;
  }

  .handsontable td.area {
    background-color: transparent;
  }

  .handsontable .wtBorder.area.corner {
    width: 4px !important;
    height: 4px !important;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    -webkit-pointer-events: none;
    -moz-pointer-events: none;
    -ms-pointer-events: none;
    pointer-events: none;
  }

  div.htBorders > div:nth-child(1) {
    left: -1px !important;
  }

  .htBorders div:first-child div:first-child{
    display: inline-block !important;
    width: 0px;
    background-color: transparent !important;
    border-left: 1px dashed #999999;
  }

  .htBorders div:first-child div:nth-child(2){
    display: inline-block !important;
    height: 0px;
    background-color: transparent !important;
    border-top: 1px dashed #999999;
  }

  .htBorders div:first-child div:nth-child(3){
    display: inline-block !important;
    width: 0px;
    background-color: transparent !important;
    border-left: 1px dashed #67C23A;
  }

  .htBorders div:first-child div:nth-child(4){
    display: inline-block !important;
    height: 0px;
    background-color: transparent !important;
    border-top: 1px dashed #67C23A;
  }

  .htBorders div.wtBorder.area:nth-child(2),
  .htBorders div.wtBorder.area:nth-child(3),
  .htBorders div.wtBorder.area:nth-child(5),
  .htBorders div.wtBorder.current:nth-child(3),
  .htBorders div.wtBorder.current:nth-child(5){
    margin-top: 1px;
    background: #1fbb7d !important;
  }

  .handsontable.ht_clone_top table.htCore,
  .handsontable.ht_master table.htCore,
  .htBorders div.wtBorder.area,
  .htBorders div.wtBorder.current{
    margin-left: 1px;
  }

  .htBorders div.wtBorder.area:nth-child(1),
  .htBorders div.wtBorder.area:nth-child(4),
  .htBorders div.wtBorder.area:nth-child(5),
  .htBorders div.wtBorder.current:nth-child(4),
  .htBorders div.wtBorder.current:nth-child(5) {
    margin-left: 2px;
    background: #1fbb7d !important;
  }

  .htBorders div.wtBorder.current:nth-child(1),
  .htBorders div.wtBorder.current:nth-child(2) {
    background: #1fbb7d !important;
  }

  .handsontableInput {
    -webkit-box-shadow: 0 0 0 2px #1fbb7d inset;
    box-shadow: 0 0 0 2px #1fbb7d inset;
  }

  .htMenu.htContextMenu {
    color: #3d4757;
    font-size: 12px;
    margin: 0;
    background: rgb(255, 255, 255);
    box-shadow: rgba(56, 56, 56, 0.2) 0px 2px 12px 0px;
    border: 1px solid rgb(226, 230, 237);
    border-radius: 4px;
    margin-left: 8px;
  }

  .htContextMenu .wtHolder {
    width: 210px !important;
  }

  .htContextMenu .wtHider {
    margin: 5px;
  }

  .htContextMenu table tbody tr td.htSeparator {
    border-top: 1px solid #e2e6ed;
  }

  .htContextMenu table tbody tr td .htItemWrapper {
    padding-left: 24px;
    position: relative;
  }

  .htContextMenu .htItemWrapper svg {
    vertical-align: text-bottom;
    margin-left: -24px;
    margin-top: 1px;
    position: absolute;
  }

  .htContextMenu .htItemWrapper .shortcut {
    float: right;
    margin-right: -4px;
    color: #767c85;
  }

  .htContextMenu .htItemWrapper .contextMenu-icons {
    width: 16px;
    height: 1px;
    margin-right: 8px;
    display: inline-block;
  }

  .htContextMenu table.htCore {
    border: 0px;
    margin-left: 0px !important;
  }

  .htContextMenu table tbody tr td.current,
  .htContextMenu table tbody tr td.zeroclipboard-is-hover {
    background: #f5f7fa;
  }

  .handsontable .htSubmenu :after {
    font-family: element-icons;
    content: "\e6e0";
    color: #777;
    right: -6px;
  }

  .handsontable th, .handsontable td {
    height: auto;
    line-height: unset;
  }

  .ht_clone_top.handsontable .wtHolder {
    overflow: hidden;
  }

  fieldset {
    padding: 10px;
    border: solid 1px #dddddd;
    border-radius: 3px;
  }

  .condition-dialog fieldset {
    height: 600px;
    width: 170px;
    display: inline-block;
  }

  legend {
    width: auto;
    margin-bottom: 1px;
    border-bottom: none;
    font-size: inherit;
    color: #4b4b4b;
    padding: 0px 5px;
  }

  .condition-dialog .condition-name-block {
    padding: 5px 4px;
    cursor: pointer;
    border-radius: 1px;
  }

  .condition-dialog .condition-name-block-selected {
    background: #409EFF;
    color: #FFFFFF;
  }

  .condition-dialog .condition-name-block:hover {
    background: #F56C6C;
    color: #FFFFFF;
  }

  .CodeMirror-lint-tooltip {
    z-index: 3000;

  }

  .property-row-item {
    height: 28px;
    margin-top: 5px;
  }

  .property-row-item-label {
    line-height: 28px;
  }

  .property-row-item-label.second {
    text-align: center;
  }

  #container {
    margin: 0px;
    padding: 0px;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .sheet-toolbar-btns {
    padding-top: 8px;
    padding-left: 5px;
    color: #606266;
  }

  .sheet-toolbar-btns .el-dropdown {
    color: #606266;
  }

  .sheet-toolbar-btn {
    height: 26px;
    width: 26px;
    cursor: pointer;
    display: inline-block;
    border: 1px solid #dcdfe6;
    border-color: #f9f9f9;
    position: relative;
    border-radius: 2px;
    margin-right: 5px
  }

  .sheet-toolbar-btn:hover,
  .sheet-toolbar-btn.active {
    background: #E4E7ED;
  }

  .sheet-toolbar-btn:hover .sheet-toolbar-dropdown-icon {
    background: #DCDFE6;
  }

  .sheet-toolbar-icon {
    position: absolute;
    top: 0px;
    left: 0px;
    bottom: 0px;
    right: 0px;
    margin: auto;
    height: 17px;
    width: 16px;
  }

  .sheet-toolbar-btn.dropdown {
    margin-right: 15px;
  }

  .sheet-toolbar-dropdown-icon {
    position: absolute;
    height: 24px;
    right: -14px;
    top: 0px;
    padding: 0px 2px;
    border-radius: 1px;
  }

  .sheet-toolbar-icon i {
    position: absolute;
    top: 0px;
  }

  .sheet-toolbar-btn.dropdown .el-dropdown {
    position: absolute;
    right: 0px;
    height: 22px;
  }

  .caret {
    display: inline-block;
    width: 0;
    height: 0;
    margin: 0px 2px;
    border-top: 4px dashed;
    border-top: 4px solid;
    border-right: 4px solid transparent;
    border-left: 4px solid transparent;
  }

  .x-spreadsheet-color-palette-cell {
    width: 20px;
    height: 20px;
    border: 1px solid #FFFFFF;
    cursor: pointer;
  }

  .x-spreadsheet-color-palette-cell:hover {
    border-color: #DCDFE6;
  }
  .div-block-clearance .el-form-item {
    margin-bottom: 10px;
  }
  .report-tilte {
    position: absolute;
    top:14px;
    right:10px;
    color: #606266;
    font-size: 12px;
  }
  .aside-open-close {
    position: absolute;
    right: -12px;
    top: 45%;
    width: 12px;
    height: 48px;
    line-height: 48px;
    color: #fff;
    background-color: #C0C4CC;
    border-radius: 0 6px 6px 0;
    font-size: 12px;
    z-index: 1000;
    cursor: pointer;
  }

  .aside-open-close.right {
    border-radius: 6px 0px 0px 6px;
    left: -14px;
  }

  .aside-open-close:hover{
    background-color: #606266;
  }

  #copy_border_block {
    width: 0px;
    height: 0px;
    z-index: 10;
  }

  #dash_white {
    animation: move 19s infinite linear;
  }

  @keyframes move {
    0% {
      stroke-dashoffset: 0;
    }
    100% {
      stroke-dashoffset: -1000;
    }
  }

  #float_image_block {
    z-index: 100;
  }

  #copy_border_block,#float_image_block {
    position: absolute;
    top: 0px;
    left: 0px;
  }

  .et-input-number:last-child {
    margin-left: auto;
  }

  .et-input-number {
    display: flex;
    width: 76px;
    height: 20px;
    float: right;
    border: 1px solid #e2e6ed;
    position: absolute;
    top: -2px;
    right: -4px;
  }

  .et-input-number .input-number-wrap {
      flex-shrink: 1;
  }

  .et-input-number input {
      display: block;
      width: 100%;
      height: 100%;
      line-height: 22px;
      border-radius: 2px;
      padding: 0 6px;
      border: none;
      outline: none;
  }

  .et-input-number .input-btns-wrap {
      cursor: pointer;
      background: #fff;
      position: relative;
      width: 15px;
      border-left: 1px solid #e2e6ed;
      border-radius: 0 2px 2px 0;
      flex-shrink: 0;
  }

  .et-input-number .input-btns-wrap .input-btn {
      display: block;
      width: 100%;
      height: 11px;
      position: relative;
  }

  .et-input-number .input-btns-wrap .input-btn:hover {
    background: #E4E7ED;
  }

  .et-input-number .input-btns-wrap .input-btn.disabled {
      pointer-events: inherit;
      opacity: .3;
      background: #fff !important;
  }

  .et-input-number .input-btns-wrap .input-btn:after {
      left: calc(50% - 3px);
  }

  .et-input-number .input-btns-wrap .input-btn.minus:after {
      position: absolute;
      top: calc(50% - 3px / 2);
      content: "";
      width: 0;
      height: 0;
      right: 2px;
      border-color: #767c85 transparent transparent;
      border-style: solid solid none;
      border-width: 3px 3px 0;
      transition: transform .3s;
  }

  .et-input-number .input-btns-wrap .input-btn.add:after {
      position: absolute;
      top: calc(50% - 3px / 2);
      content: "";
      width: 0;
      height: 0;
      border-color: transparent transparent #767c85;
      border-style: none solid solid;
      border-width: 0 3px 3px;
      transition: transform .3s;
  }

  .component-header {
    height: 24px;
    background: #f2f4f7;
    white-space: nowrap;
    font-size: 12px;
    text-align: center;
    border-bottom: 1px solid #e2e6ed;
  }

  .component-header-left {
    display: inline-flex;
    align-items: center;
    vertical-align: top;
    height: 100%;
    padding: 0 8px;
  }

  .component-header-middle {
    display: inline-flex;
    align-items: center;
    height: 100%;
  }

  .component-header-right {
    display: inline-flex;
    flex-direction: row-reverse;
    vertical-align: top;
    height: 100%;
    padding: 0 8px;
    float: right;
    line-height: 56px;
  }

  .component-header-middle.s .tab-btn.selected {
    background: #1fbb7d;
  }
  .component-header-middle .tab-btn:first-child {
    margin-left: 0;
  }
  .component-header-middle .tab-btn.selected {
    color: #fff;
    background: #3a72df;
  }
  .component-header-middle .tab-btn {
    position: relative;
    display: inline-block;
    height: 20px;
    line-height: 22px;
    padding: 0 10px;
    border-radius: 2px;
    margin-left: 8px;
    cursor: pointer;
  }

  .component-header-middle .tab-btn:hover {
    background-color: #DCDFE6;
  }

  .component-header-middle .tab-btn:after {
    position: absolute;
    content: "";
    left: -4px;
    right: -4px;
    top: -12px;
    bottom: -12px;
  }

  #designerRightPanel {
    background: #f2f4f7;
    border-left: 1px solid #e2e6ed;
    position: absolute;
  }

  #designerRightPanel .el-tabs__item {
    padding: 0 12px;
  }

  .tool-dialog .el-tabs--left .el-tabs__header.is-left {
    margin-right: 0px;
  }

  .tool-dialog .el-tabs--left .el-tabs__active-bar.is-left,
  .tool-dialog .el-tabs--left .el-tabs__nav-wrap.is-left::after {
    width: 1px;
  }

  .tool-dialog .el-tabs__content,
  .tool-dialog .el-tab-pane {
    height: 100%;
  }

  .el-dialog__wrapper {
    pointer-events: none;
    overflow: hidden;
  }

  .el-dialog {
    pointer-events: auto;
  }

  .tool-dialog .el-dialog__header {
    background: #f2f4f7;
  }

  .tool-dialog .el-dialog__body {
    padding: 0px;
  }

  .float-image-box {
    position: absolute;
    top: 100px;
    left: 100px;
  }
  /*四边*/
  .float-image-box .t,
  .float-image-box .b,
  .float-image-box .l,
  .float-image-box .r {
      position: absolute;
      z-index: 1;
      background: transparent;
  }

  .float-image-box .l,
  .float-image-box .r {
      width: 5px;
      height: 100%;
      cursor: col-resize;
  }

  .float-image-box .t,
  .float-image-box .b {
      width: 100%;
      height: 5px;
      cursor: row-resize;
  }

  .float-image-box .t,
  .float-image-box .tl,
  .float-image-box .tr {
      top: 0px;
  }

  .float-image-box .b,
  .float-image-box .br,
  .float-image-box .bl {
      bottom: 0px;
  }

  .float-image-box .l,
  .float-image-box .tl,
  .float-image-box .bl {
      left: 0px;
  }

  .float-image-box .r,
  .float-image-box .tr,
  .float-image-box .br {
      right: 0px;
  }

  /*四角*/
  .float-image-box .tl,
  .float-image-box .bl,
  .float-image-box .br,
  .float-image-box .tr {
      width: 20px;
      height: 20px;
      position: absolute;
      background: transparent;
      z-index: 2;
      cursor: nwse-resize
  }

  .float-image-box .tr,
  .float-image-box .bl {
      cursor: nesw-resize;
  }

  /*图片*/
  .float-image-box img {
    width: 100%;
    height: 100%;
  }

  .float-image-toolbar {
    width: 20px;
    height: 20px;
    top: 0px;
    right: 0px;
    line-height: 20px;
    text-align: center;
    position: absolute;
    cursor: pointer;
    z-index: 2;
  }
</style>
