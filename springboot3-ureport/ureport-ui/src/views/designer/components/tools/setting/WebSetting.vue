<template>
  <div class="web-page-setting" style="display: flex;">
      <div style="width: 82px;">
        <div style="margin-bottom: 12px;">
          <el-radio-group v-model="buttonType" size="mini">
            <el-radio-button label="工具栏" ></el-radio-button>
          </el-radio-group>
        </div>
        <div style="margin-bottom: 12px;">
          <el-radio-group v-model="buttonType" size="mini" style="width: 68px;">
            <el-radio-button label="打印" >打&nbsp;印</el-radio-button>
          </el-radio-group>
        </div>
        <div style="margin-bottom: 12px;">
          <el-radio-group v-model="buttonType" size="mini" style="width: 68px;">
            <el-radio-button label="导出" >导&nbsp;出</el-radio-button>
          </el-radio-group>
        </div>
        <div style="margin-bottom: 12px;">
        <el-radio-group v-model="buttonType" size="mini" style="width: 68px;">
          <el-radio-button label="分页" >分&nbsp;页</el-radio-button>
        </el-radio-group>
        </div>
      </div>
      <div style="flex:1;">
        <div class="web-setting-split-item">
          <div style="flex: 1;">
            <el-checkbox v-model="paper.print">显示工具栏</el-checkbox>
          </div>
          <div>
            <span style="margin-left: 12px;">显示位置：</span>
            <el-select v-model="paper.verticalAlign" size="mini">
              <el-option
                v-for="(item,index) in htmlReportAlignOptions"
                :key="index+1"
                :value="item.value"
                :label="item.label"
              />
            </el-select>
          </div>
        </div>
        <div class="web-setting-split-item">
          <div style="flex: 1;">
            <el-checkbox v-model="paper.print">打印</el-checkbox>
          </div>
          <div>
            <span style="margin-left: 12px;">显示位置：</span>
            <el-select v-model="paper.verticalAlign" size="mini">
              <el-option
                v-for="(item,index) in htmlReportAlignOptions"
                :key="index+1"
                :value="item.value"
                :label="item.label"
              />
            </el-select>
          </div>
        </div>
        <div class="web-setting-split-item">
          <div style="flex: 1;">
          <el-checkbox v-model="paper.print">Word</el-checkbox>
          <el-checkbox v-model="paper.print">Excel</el-checkbox>
          <el-checkbox v-model="paper.print">PDF</el-checkbox>
          </div>
          <div>
            <span style="margin-left: 12px;">显示位置：</span>
            <el-select v-model="paper.verticalAlign" size="mini">
              <el-option
                v-for="(item,index) in htmlReportAlignOptions"
                :key="index+1"
                :value="item.value"
                :label="item.label"
              />
            </el-select>
          </div>
        </div>
        <div class="web-setting-split-item">
          <el-checkbox v-model="paper.print">总条数</el-checkbox>
            <el-checkbox v-model="paper.print">首页</el-checkbox>
            <el-checkbox v-model="paper.print">上一页</el-checkbox>
            <el-checkbox v-model="paper.print">下一页</el-checkbox>
            <el-checkbox v-model="paper.print">尾页</el-checkbox>
            <div>
              <span>显示位置：</span>
              <el-select v-model="paper.verticalAlign" size="mini">
                <el-option
                  v-for="(item,index) in htmlReportAlignOptions"
                  :key="index+1"
                  :value="item.value"
                  :label="item.label"
                />
              </el-select>
            </div>
        </div>
      </div>
  </div>

</template>

<script>
import { undoManager, pointToMM, mmToPoint, buildPageSizeList } from '../../Utils.js'
import { getImages } from '@/api/designer'

export default {
  name: 'PageSettings',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      buttonType: '工具栏',
      dialogVisible: false,
      urls: [],
      paper: {
        paperType: 'A4'
      },
      checkList: [],
      backgroundOptions: [
        {label: '颜色', value: 'color'},
        {label: '图片', value: 'image'}
      ],
      location: {
        width: 0,
        height: 0,
        leftMargin: 0,
        rightMargin: 0,
        topMargin: 0,
        bottomMargin: 0
      },
      paperSizeList: [],
      paperTypeOptions: [
        { label: 'A3', value: 'A3' },
        { label: 'A4', value: 'A4' },
        { label: 'A5', value: 'A5' },
        { label: 'A6', value: 'A6' },
        { label: 'B3', value: 'B3' },
        { label: 'B4', value: 'B4' },
        { label: 'B5', value: 'B5' },
        { label: 'B6', value: 'B6' },
        { label: '自定义', value: 'CUSTOM' }
      ],
      orientationOptions: [
        { label: '纵向', value: 'portrait' },
        { label: '横向', value: 'landscape' }
      ],
      verticalAlignOptions: [
        { label: '顶部', value: 'top' },
        { label: '中部', value: 'center' },
        { label: '底部', value: 'bottom' },
      ],
      htmlReportAlignOptions: [
        { label: '居左', value: 'left' },
        { label: '居中', value: 'center' },
        { label: '居右', value: 'right' }
      ]
    }
  },
  watch: {
    context: function(val) {
      this.init()
    }
  },
  mounted: function() {
    this.paperSizeList = buildPageSizeList()
    this.init()
  },
  methods: {
    init() {
      this.paper = this.context.reportDef.paper
      if (this.paper.paperType !== 'CUSTOM') {
        const pageSize = this.paperSizeList[this.paper.paperType]
        this.location.width = pageSize.width
        this.location.height = pageSize.height
      } else {
        this.location.width = pointToMM(this.paper.width)
        this.location.height = pointToMM(this.paper.height)
      }
      this.location.leftMargin = pointToMM(this.paper.leftMargin)
      this.location.rightMargin = pointToMM(this.paper.rightMargin)
      this.location.topMargin = pointToMM(this.paper.topMargin)
      this.location.bottomMargin = pointToMM(this.paper.bottomMargin)
    },
    handlePaperTypeChange(value) {
      if (value !== 'CUSTOM') {
        const pageSize = this.paperSizeList[value]
        this.paper.width = mmToPoint(pageSize.width)
        this.paper.height = mmToPoint(pageSize.height)
        this.location.width = pageSize.width
        this.location.height = pageSize.height
        this.context.printLine.refresh()
      }
    },
    handlePaperSizeChange(value) {
      this.paper.width = mmToPoint(this.location.width)
      this.paper.height = mmToPoint(this.location.height)
      this.context.printLine.refresh()
    },
    handleOrientationChange(value) {
      this.context.printLine.refresh()
    },
    handlemLeftMargin(val) {
      this.paper.leftMargin = mmToPoint(val)
    },
    handlemRightMargin(val) {
      this.paper.rightMargin = mmToPoint(val)
    },
    handlemTopMargin(val) {
      this.paper.topMargin = mmToPoint(val)
    },
    handlemBottomMargin(val) {
      this.paper.bottomMargin = mmToPoint(val)
    },
    handleImage() {
      getImages({}).then(response => {
        const report = response.data || []
        const urls = []
        for (const url of report) {
          urls.push(url)
        }
        this.urls = urls
        this.dialogVisible = true
      })
    },
    handleSaveImage(url) {
      this.paper.bgImage = url
      this.setBackGroundImage(url)
      this.dialogVisible = false
    },
    setBackGroundImage(url) {
      const ele = document.getElementById('designer')
      const master = ele.children[0]
      const wtHolder = master.children[0]
      const wtHider = wtHolder.children[0]
      if (url) {
        wtHider.style.background = 'url(' + url + ') 52px 27px no-repeat'
      } else {
        wtHider.style.background = 'transparent'
      }
    },
    handleChange(val) {
      this.setBackGroundImage(val)
    }
  }
}
</script>

<style>
  .web-page-setting .el-radio-button--mini,
  .web-page-setting .el-radio-button__inner {
    width: 100%;
  }

  .web-setting-split-item {
    display: flex;
    align-items: center;
    border-bottom: 1px solid #dcdfe6;
    padding-bottom: 14px;
    margin-bottom: 14px;
  }
</style>
