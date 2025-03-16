<template>
  <el-dialog
    title="SpringBean数据源配置"
    :visible.sync="springBeanDataSetValue.dialogVisible"
    append-to-body
    :close-on-click-modal="false"
    width="400px"
  >
  <el-form ref="springRuleForm" :rules="rules" :model="form" size="small" label-width="100px">
    <el-form-item label="数据源名称" prop="name">
      <el-input v-model="form.name"></el-input>
    </el-form-item>
    <el-form-item label="Bean ID" prop="beanId">
      <el-input v-model="form.beanId"></el-input>
    </el-form-item>
  </el-form>
  <span slot="footer" class="dialog-footer">
     <el-button size="small" @click="springBeanDataSetValue.dialogVisible = false">取 消</el-button>
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
    springBeanDataSetValue: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      edit: false,
      datasource: null,
      headers: [],
      parameters: [],
      form: {
        name: '',
        beanId: '',
      },
      rules: {
        name: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' },
        ],
        beanId: [
          { required: true, message: '请输Bean ID', trigger: 'blur' },
        ]
      },
    }
  },
  watch: {
    springBeanDataSetValue: function(val) {
      this.init(val)
    }
  },
  mounted: function() {
    this.init(this.springBeanDataSetValue)
  },
  methods: {
    init(val) {
      if (val.context && val.context.reportDef) {
        if (val.edit) {
          this.datasource = val.datasource
          this.edit = true
          this.form.name = this.datasource.name
          this.form.beanId = this.datasource.beanId
        } else {
          this.edit = false
          this.datasource = null
          this.form.name = ''
          this.form.beanId = ''
        }
      }
    },
    saveDataSource() {
      this.$refs['springRuleForm'].validate((valid) => {
        if (valid) {
          const datasources = this.springBeanDataSetValue.context.reportDef.datasources || []
          const obj = datasources.find(item => item.name === this.form.name)
          if (obj) {
            if(!this.edit || (this.edit && obj.name != this.datasource.name)) {
              this.$message.error(`已创建数据源${obj.name}`)
              return
            }
          }
          if(this.edit) {
            let name = this.datasource.name
            let obj = datasources.find(e => e.name === name)
            const ds = {
              name: this.form.name,
              type: 'spring',
              beanId: this.form.beanId,
              datasets: obj.datasets
            }
            let index = datasources.findIndex(e => e.name === name)
            datasources.splice(index,1,ds)
          } else {
            datasources.push({
              name: this.form.name,
              type: 'spring',
              beanId: this.form.beanId,
              datasets: []
            })
          }
          this.springBeanDataSetValue.dialogVisible = false
        }
      })
    }
  }
}
</script>
