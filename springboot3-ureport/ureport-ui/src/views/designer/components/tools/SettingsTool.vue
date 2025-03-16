<template>
  <div class="component-icon-btn" @click="openSettings">
    <div class="component-icon-btn-icon"><i class="el-icon-setting" /></div>
    <el-dialog
      title="报表配置"
      :visible.sync="dialogVisible"
      append-to-body
      :close-on-click-modal="false"
      top="40px"
      width="640px"
    >
      <el-tabs stretch>
        <el-tab-pane label="页面配置">
          <PageSettings :context="context" />
        </el-tab-pane>
        <el-tab-pane label="页眉页脚">
          <HeaderAndFooterSetting :context="context" />
        </el-tab-pane>
        <el-tab-pane label="分页分栏">
          <PaginationSetting :context="context" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import PageSettings from './setting/PageSetting'
import HeaderAndFooterSetting from './setting/HeaderAndFooterSetting'
import PaginationSetting from './setting/PaginationSetting'

import { undoManager, pointToMM, mmToPoint, buildPageSizeList } from '../Utils.js'

export default {
  name: 'SettingsTool',
  components: {
    PageSettings,
    HeaderAndFooterSetting,
    PaginationSetting
  },
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      dialogVisible: false
    }
  },
  mounted: function() {

  },
  methods: {
    openSettings() {
      this.dialogVisible = true
    }
  }
}
</script>

<style>
</style>
