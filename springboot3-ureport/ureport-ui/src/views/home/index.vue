<template>
  <div style="display: flex;flex-direction: column;padding: 10px;width: 100%;height: 100%;position: absolute;">
    <div class="box-card" style="margin-bottom: 10px;">
      <el-form :inline="true" :model="form" size="small">
        <el-form-item label="模板名称">
          <el-input v-model="form.name" placeholder="模板名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="box-card" style="flex: 1;display: flex;flex-direction: column;overflow: hidden;">
    <div style="padding-bottom: 10px;">
      <el-button type="primary" size="small" @click="handleCreate">新建模板</el-button>
    </div>
    <el-table
    size="small"
    v-if="reportFiles.length > 0"
    :height="400"
    :data="reportFiles"
    border
    :header-cell-style="()=>'background: #f5f7fa'"
    style="width: 100%;flex: 1;">
      <el-table-column prop="name" label="报表文件名称" />
      <el-table-column prop="updateDate" label="更新时间" width="160" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-link  :underline="false" type="primary" icon="el-icon-view"><router-link target="_blank" :to="{path: '/ureport/html/'+ scope.row.name}"><span style="font-size: 12px;">预览</span></router-link></el-link>
          <el-link  :underline="false" type="primary" icon="el-icon-edit" style="margin-left: 16px;"><router-link target="_blank" :to="{path: '/ureport/designer/'+ scope.row.name}"><span style="font-size: 12px;">编辑</span></router-link></el-link>
          <el-link  :underline="false" type="primary" icon="el-icon-delete" @click="handleRemove(scope.row)" style="margin-left: 16px;"><span style="font-size: 12px;">删除</span></el-link>
        </template>
      </el-table-column>
    </el-table>
    </div>
  </div>
</template>

<script>
import { getReports, deleteReportFile } from '@/api/designer'

export default {
  data: function() {
    return {
      form: {},
      dialogVisible: false,
      loading: false,
      value: '',
      fileName: '',
      reportFiles: []
    }
  },
  mounted: function() {
    this.handleOpen()
  },
  methods: {
    handleCreate() {
      this.$router.push({path:'/ureport/designer'})
    },
    async copyTextToClipboard(text) {
      try {
        await navigator.clipboard.writeText(text);
        console.log('Text copied to clipboard');
      } catch (err) {
        console.error('Failed to copy: ', err);
      }
    },
    onSubmit() {

    },
    handleOpen() {
      this.loading = true
      this.reportFiles = []
      getReports({}).then(res => {
        this.reportFiles = res.data || []
        this.loading = false
      })
      this.dialogVisible = true
    },
    handleRemove(row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const that = this
        const fullFile = 'file:' + row.name
        deleteReportFile({ file: fullFile }).then(response => {
          const index = that.reportFiles.indexOf(row)
          that.reportFiles.splice(index, 1)
        })
      })
    }
  }
}
</script>

