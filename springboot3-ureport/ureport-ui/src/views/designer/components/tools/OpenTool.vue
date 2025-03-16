<template>
  <div class="component-icon-btn" @click="handleOpen">
    <div class="component-icon-btn-icon" style="line-height: 18px;font-size: 17px;">
      <i class="el-icon-folder-opened"></i>
    </div>
    <el-dialog title="报表文件列表"
    :visible.sync="dialogVisible"
    append-to-body
    :close-on-click-modal="false"
    width="800px"
    >
      <div
      v-loading="loading"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      style="height: 460px;">
        <el-button @click="handleCreate" size="small" style="margin-bottom: 10px;">新建报表模板</el-button>
        <el-table
        size="small"
        v-if="reportFiles.length > 0"
        :height="400"
        :data="reportFiles"
        border
        :header-cell-style="()=>'background: #f5f7fa'"
        style="width: 100%;">
          <el-table-column prop="name" label="报表文件名称" />
          <el-table-column prop="updateDate" label="更新时间" width="160" align="center" />
          <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
                <el-link  :underline="false" type="primary" icon="el-icon-view"><router-link target="_blank" :to="{path: '/ureport/html/'+ scope.row.name}"><span style="font-size: 12px;">预览</span></router-link></el-link>
                <el-link  :underline="false" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)" style="margin-left: 24px;"><span style="font-size: 12px;">编辑</span></el-link>
                <el-link  :underline="false" type="primary" icon="el-icon-delete" @click="handleRemove(scope.row)" style="margin-left: 24px;"><span style="font-size: 12px;">删除</span></el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getReports, deleteReportFile } from '@/api/designer'

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
      loading: false,
      value: '',
      fileName: '',
      reportFiles: []
    }
  },
  mounted: function() {

  },
  methods: {
    handleOpen() {
      this.loading = true
      this.reportFiles = []
      getReports({}).then(res => {
        this.reportFiles = res.data || []
        this.loading = false
      })
      this.dialogVisible = true
    },
    handleCreate() {
      this.$router.push({path:'/ureport/designer'})
      this.dialogVisible = false
    },
    handlePreview(row) {
      this.$router.push({path:`/ureport/html/${row.name}`,query:{}})
    },
    handleEdit(row) {
      const fileName = row.name
      this.$router.push({path:'/ureport/designer/' + row.name})
      this.dialogVisible = false
    },
    handleRemove(row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const that = this
        const fullFile = row.name
        deleteReportFile({ file: fullFile }).then(response => {
          const index = that.reportFiles.indexOf(row)
          that.reportFiles.splice(index, 1)
        })
      })
    }
  }
}
</script>
