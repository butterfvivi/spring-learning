<template>
  <el-dialog
    title="SpringBean数据集配置"
    :visible.sync="springBeanDataSetValue.dialogVisible"
    append-to-body
    :close-on-click-modal="false"
    width="600px"
  >
  <el-form ref="springRuleForm" :rules="rules" :model="form" size="small" label-width="100px" label-position="left">
    <el-form-item label="数据集名称" prop="name">
      <el-input v-model="form.name" placeholder="数据集名称"></el-input>
    </el-form-item>
    <el-form-item label="方法名称" prop="method">
      <el-input v-model="form.method" placeholder="方法必须包含三个参数: String,String,Map"></el-input>
    </el-form-item>
    <el-form-item label="返回对象">
      <el-input v-model="form.clazz" placeholder="指定该方法返回类全名,用于生成字段,如不指定需手工添加字段"></el-input>
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
import { springbeanFieldList } from '@/api/datasource'

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
        method: '',
        clazz:''
      },
      rules: {
        name: [
          { required: true, message: '请输入数据源名称', trigger: 'blur' },
        ],
        method: [
          { required: true, message: '方法名称', trigger: 'blur' },
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
        this.datasource = val.datasource
        if (val.edit) {
          this.dataset = val.dataset
          this.edit = true
          this.form.name = this.dataset.name
          this.form.method = this.dataset.method
          this.form.clazz = this.dataset.clazz
          this.parameters = dataset.parameters
        } else {
          this.edit = false
          this.dataset = null
          this.form.name = ''
          this.form.method = ''
          this.form.clazz = ''
          this.parameters = []
        }
      }
    },
    addDataSetParam() {
      this.dataSetParameterValue = {
        dialogVisible: true,
        edit: false,
        parameters: this.parameters
      }
    },
    async saveDataSource() {
      const valid = await this.$refs['springRuleForm'].validate()
      if (valid) {
        const form = this.form
        for (const index in this.datasources) {
          const datasource = datasources[index]
          const datasets = datasource.datasets || []
          const old = datasets.find(ele => ele.name === form.name)
          if (old && (!this.springBeanDataSetValue.edit || (this.dataset && this.dataset.name !== form.name))) {
            this.$message('数据集名称重复')
            return
          }
        }
        let fields = []
        if(form.clazz) {
          const res = await springbeanFieldList({clazz: form.clazz})
          fields = res.data || []
        }
        if(this.springBeanDataSetValue.edit) {
          let index = this.datasource.datasets.indexOf(this.dataset)
          const ds = {
            name: form.name,
            method: form.method,
            clazz: form.clazz,
            fields: this.dataset.fields || []
          }
          this.datasource.datasets.splice(index,1,ds)
        } else {
          this.datasource.datasets.push({
            name: form.name,
            method: form.method,
            clazz: form.clazz,
            fields: fields
          })
        }
        this.springBeanDataSetValue.dialogVisible = false
      }
    }
  }
}
</script>
