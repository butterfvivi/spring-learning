<template>
  <el-dialog
    title="API数据源配置"
    :visible.sync="apiDataSetValue.dialogVisible"
    append-to-body
    :close-on-click-modal="false"
    width="600px"
  >
  <el-form :rules="rules" ref="apiRuleForm" :model="form" label-position="left" label-width="100px" size="small" style="margin-bottom: 10px;">
    <el-form-item label="数据源名称:" prop="name">
      <el-input v-model="form.name"></el-input>
    </el-form-item>
    <el-form-item label="请求地址:" prop="url">
      <el-input v-model="form.url"></el-input>
    </el-form-item>
    <el-form-item label="请求头:" prop="method">
      <div style="height: 200px;">
        <div v-for="(row,index) in headers" style="display: flex;">
          <div style="flex: 1;">
            <el-input v-model="row.name" placeholder="名称" size="mini"></el-input>
          </div>
          <div style="flex: 1;margin-left: 12px;">
            <el-input v-model="row.value" placeholder="值" size="mini"></el-input>
          </div>
          <div style="margin-left: 8px;">
            <i @click="addRow(index)" class="el-icon-circle-plus-outline" style="cursor: pointer;"></i>
            <i @click="removeRow(index)" class="el-icon-remove-outline" style="cursor: pointer;margin-left: 4px;"></i>
          </div>
        </div>
      </div>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
     <el-button size="small" @click="apiDataSetValue.dialogVisible = false">取 消</el-button>
    <el-button size="small" type="primary" @click="saveDataSource">确 定</el-button>
  </span>
  </el-dialog>
</template>

<script>
import { selectBuildinDruidList } from '@/api/datasource'
import DataSetParameterDialog from './DataSetParameterDialog'
import HeadersDialog from './HeadersDialog'
import { previewApiData } from '@/api/datasource'

export default {
  components: {
    DataSetParameterDialog,
    HeadersDialog
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
      headers: [],
      form: {
        name: '',
        url: '',
        method: 'POST'
      },
      rules: {
        name: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' },
        ],
        url: [
          { required: true, message: '请输入请求连接', trigger: 'blur' },
        ],
        method: [
          { required: true, message: '请选择请求方式', trigger: 'blur' },
        ],
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
          const datasource = val.datasource
          this.datasource = datasource
          this.form.name = datasource.name
          this.form.url = datasource.url
          this.form.method = datasource.method
          this.headers = datasource.headers || [{
            name: '',
            value: '',
          }]
        } else {
          this.edit = false
          this.datasource = null
          this.form.name = ''
          this.form.url = ''
          this.form.method = 'POST'
          this.parameters = [],
          this.headers = [{
            name: '',
            value: '',
          }]
        }
      }
    },
    addRow(index) {
      this.headers.splice(index + 1, 0, {
        name: '',
        value: '',
      })
    },
    removeRow(index) {
      if(index === 0) {
        this.headers.splice(index, 1, {
          name: '',
          value: '',
        })
      } else {
        this.headers.splice(index, 1)
      }
    },
    saveDataSource() {
      this.$refs['apiRuleForm'].validate((valid) => {
        if (valid) {
          const datasources = this.apiDataSetValue.context.reportDef.datasources || []
          const obj = datasources.find(item => item.name === this.form.name)
          if (obj) {
            if(!this.edit || (this.edit && obj.name != this.datasource.name)) {
              this.$message.error(`已创建数据源${obj.name}`)
              return
            }
          }
          const ds = {
            name: this.form.name,
            method: this.form.method,
            type: 'api',
            url: this.form.url,
            parameters: this.parameters,
            headers: this.headers,
            datasets: []
          }
          if(this.edit) {
            let index = datasources.indexOf(this.datasource)
            datasources.splice(index, 1, ds)
          } else {
            datasources.push(ds)
          }
          this.apiDataSetValue.dialogVisible = false
        }
      })
    },
    buildDataSet(object) {
      let name = object.name
      let data = object.data
      if(data && data.length > 0) {
        const keys = Object.keys(data[0])
        const dataset = {
          name: name,
          fields: []
        }
        for(const key of keys) {
          dataset.fields.push({
            name: key,
            type: null,
          })
        }
        return dataset
      }
      return null
    },
  }
}
</script>
