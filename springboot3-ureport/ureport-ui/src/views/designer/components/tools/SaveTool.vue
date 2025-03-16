<template>
  <el-dropdown
  trigger="click"
  @command="handleOpen"
  style="float: left;">
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <i class="icons icons-16 icons-16-save"></i>
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn"></div>
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="save">
        <i style="vertical-align: text-bottom;display: inline-block" class="icons icons-16 icons-16-save"></i>
        保存
      </el-dropdown-item>
      <el-dropdown-item command="saveAs">
        <svg style="vertical-align: text-top;margin-right: 5px;width: 14px;height: 14px;fill: currentColor;overflow: hidden;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="13313"><path d="M56.878545 0h796.439273l154.018909 154.018909c10.705455 10.658909 16.663273 25.134545 16.663273 40.215273v772.887273c0 31.418182-25.460364 56.878545-56.878545 56.878545H56.878545A56.878545 56.878545 0 0 1 0 967.121455V56.878545C0 25.460364 25.460364 0 56.878545 0zM227.607273 56.878545v284.439273h512v-284.392727h-512zM170.682182 512v398.242909h682.635636V512H170.682182z m398.196363-398.242909h113.803637v170.682182h-113.803637V113.757091z" fill="#767676" p-id="13314"></path></svg>
        另存为
      </el-dropdown-item>
    </el-dropdown-menu>
    <el-dialog
      :title="type==='save'? '保存报表': '另存为报表'"
      :visible.sync="dialogVisible"
      append-to-body
      :close-on-click-modal="false"
      width="400px"
    >
      <el-form ref="saveReprotRuleForm" :rules="rules" :model="form" label-width="90px" size="small">
        <el-form-item label="报表名称:" prop="value">
          <el-input v-model="form.value" placeholder="请输入内容" size="small" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" size="small" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </el-dropdown>
</template>

<script>
import { tableToXml } from '../Utils.js'
import { saveReportFile } from '@/api/designer'

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
      type: null,
      form: {
        value: ''
      },
      rules: {
        value: [
          { required: true, message: '请输入报表名称', trigger: 'blur' }
        ]
      },
      fileName: ''
    }
  },
  mounted: function() {

  },
  methods: {
    handleOpen(command) {
      this.type = command
      this.fileName = ''
      const reportFullName = this.context.reportDef.reportFullName
      if (!reportFullName) {
        this.dialogVisible = true
      } else if (command === 'save') {
        this.fileName = reportFullName
        this.doSave()
      } else {
        this.dialogVisible = true
      }
    },
    save() {
      this.$refs['saveReprotRuleForm'].validate((valid) => {
        if (valid) {
          this.fileName = this.form.value + '.ureport.xml'
          if(this.type === 'save') {
            this.doSave()
          } else {
            this.doSaveAs()
          }
        }
      })
    },
    async doSave() {
      const oldFileName = this.context.reportDef.reportFullName
      const content = await tableToXml(this.context)
      saveReportFile({
        content,
        name: this.fileName,
        oldName: oldFileName
      }).then(response => {
        this.$router.push({path:'/ureport/designer/' + this.fileName})
        this.dialogVisible = false
        this.$message({ message: '保存成功', type: 'success' })
      })
    },
    async doSaveAs() {
      const content = await tableToXml(this.context)
      saveReportFile({
        content,
        name: this.fileName,
        oldName: ''
      }).then(response => {
        this.$router.push({path:'/ureport/designer/' + this.fileName})
        this.dialogVisible = false
        this.$message({message: '保存成功',type: 'success'})
      })
    }
  }
}
</script>
