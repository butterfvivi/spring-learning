<template>
  <div>
    <el-button size="mini" type="primary" style="margin-bottom: 10px;" @click="addDataSetParam()">添加请求头</el-button>
    <div style="height: 160px;">
      <div v-for="(header,index) in headers" style="display: flex;border-bottom: 1px solid #eee;">
        <div style="flex: 1;">
          {{header.name}}:
        </div>
        <div style="flex: 2;">
          {{header.value}}
        </div>
        <div style="width:16px;cursor: pointer;" @click="removeDataSetParam(index)">
          <i class="el-icon-delete"></i>
        </div>
      </div>
    </div>
    <el-dialog
      title="参数配置"
      append-to-body
      :visible.sync="dialogVisible"
      width="400px"
    >
      <el-form :model="form" label-width="55px" ref="datasetParamRuleForm" :rules="rules" size="small">
        <el-form-item label="名称:" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="值:" prop="value">
          <el-input v-model="form.value" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" size="small" @click="saveDataSetParam()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  props: {
    headers: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      edit: false,
      rowIndex: -1,
      dialogVisible: false,
      form: {
        name: '',
        value: '',
      },
      rules: {
        name:[{ required: true, message: '请输入参数名称', trigger: 'blur' }],
        value:[{ required: true, message: '请输入参数值', trigger: 'blur' }]
      },
      oldName: '',
    }
  },
  mounted: function() {

  },
  methods: {
    addDataSetParam() {
      this.edit = false
      this.rowIndex = -1
      this.form = {
        name: '',
        value: ''
      }
      this.dialogVisible = true
    },
    editDataSetParam(row, index) {
      this.edit = true
      this.rowIndex = index
      this.form = {
        name: row.name,
        value: row.value
      }
      this.dialogVisible = true
    },
    removeDataSetParam(index) {
      this.$confirm('确定删除参数?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.headers.splice(index, 1)
      })
    },
    saveDataSetParam() {
      this.$refs.datasetParamRuleForm.validate((valid) => {
        if (valid) {
          const headers = this.headers
          const edit = this.edit
          const name = this.form.name
          if (edit) {
            headers.splice(this.rowIndex, 1, this.form)
          } else {
            headers.push(this.form)
          }
          this.dialogVisible = false
        }
      })
    }
  }
}
</script>

<style>

</style>
