<template>
  <el-dialog
    title="添加API数据集"
    :visible.sync="apiDataSetValue.dialogVisible"
    append-to-body
    :close-on-click-modal="false"
    width="600px"
  >
    <el-form :model="form" label-position="left" label-width="100px" size="small" style="margin-bottom: 10px;">
      <el-form-item label="数据集名称:" prop="name">
        <el-input size="small" v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="请求方式">
        <el-select v-model="form.method" style="width: 100%;">
          <el-option label="GET" value="GET"></el-option>
          <el-option label="POST" value="POST"></el-option>
        </el-select>
      </el-form-item>
      <data-set-parameter-dialog :parameters="form.parameters" />
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button size="small" @click="apiDataSetValue.dialogVisible = false">取 消</el-button>
      <el-button size="small" type="primary" @click="saveDataSet">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import DataSetParameterDialog from './DataSetParameterDialog'

export default {
  components: {
    DataSetParameterDialog
  },
  props: {
    apiDataSetValue: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      edit: false,
      datasource: null,
      dataset: null,
      form: {
        name: '',
        method: 'POST',
        parameters: []
      }
    }
  },
  watch: {
    apiDataSetValue: function(val) {
      this.init(val)
    }
  },
  mounted: function() {
    this.init(this.apiDataSetValue)
  },
  methods: {
    init(val) {
      if (val.context && val.context.reportDef) {
        if (val.edit) {
          this.edit = true
          this.datasource = val.datasource
          this.dataset = val.dataset
          this.form.name = val.dataset.name
          this.form.method = val.dataset.method
          this.form.parameters = val.dataset.parameters || []
        } else {
          this.edit = false
          this.datasource = val.datasource
          this.dataset = null
          this.form.name = ''
          this.form.method = 'POST'
          this.form.parameters = []
        }
      }
    },
    saveDataSet() {
      const form = this.form
      if(!form.name) {
        this.$message('请输入数据集名称')
        return
      }
      const datasources = this.apiDataSetValue.context.reportDef.datasources || []
      for (const index in datasources) {
        const datasource = datasources[index]
        const datasets = datasource.datasets || []
        const old = datasets.find(ele => ele.name === form.name)
        if (old && (!this.edit || (this.dataset && this.dataset.name !== form.name))) {
          this.$message('数据集名称重复')
          return
        }
      }
      if(this.edit) {
        let index = this.datasource.datasets.indexOf(this.dataset)
        const ds = {
          name: form.name,
          method: form.method,
          parameters: form.parameters,
          fields: this.dataset.fields
        }
        this.datasource.datasets.splice(index,1,ds)
      } else {
        this.datasource.datasets.push({
          name: form.name,
          method: form.method,
          parameters: form.parameters,
          fields: []
        })
      }
      this.apiDataSetValue.dialogVisible = false
    },
  }
}
</script>
