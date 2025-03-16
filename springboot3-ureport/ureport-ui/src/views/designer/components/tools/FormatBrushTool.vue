<template>
  <div class="component-icon-btn" @click="handleOpen">
    <div class="component-icon-btn-icon">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><g fill="none" fill-rule="evenodd"><path d="M7.5 2v2.5H4a.5.5 0 00-.5.5v2a.5.5 0 00.5.5h9a.5.5 0 00.5-.5V5a.5.5 0 00-.5-.5H9.5V2a.5.5 0 00-.5-.5H8a.5.5 0 00-.5.5z" stroke="#3D4757"/><path fill="#3D4757" d="M13 7h1v4h-1z"/><path d="M11 13a2 2 0 002-2V8.764A3 3 0 118.764 13H11z" fill="#3D4757"/><path fill="#3D4757" d="M1 13h10v1H1z"/><path d="M1 13a2 2 0 002-2V8.764A3 3 0 011 14v-1z" fill="#3D4757"/><path fill="#3D4757" d="M3 7h1v4H3z"/></g></svg>
    </div>
  </div>
</template>
<script>
import { uploadImageTemplate } from '@/api/designer'
export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      form: {
        file: null
      },
      dialogVisible: false
    }
  },
  mounted: function() {

  },
  methods: {
    handleOpen() {
      this.form.file = null
      this.dialogVisible = true
    },
    beforeClose(done) {
      if(this.form.file) {
        this.$refs.uploadImageTemplateRef.clearFiles()
      }
      done()
    },
    changeFiles(file, fileList) {
      this.form.file = file
    },
    submitUpload() {
      if(this.form.file === null) {
        this.$message('请选择上传文件');
        return
      }
      const formData = new FormData()
      formData.append('file', this.form.file.raw)
      uploadImageTemplate(formData).then((response) => {
        this.$refs.uploadImageTemplateRef.clearFiles()
        this.dialogVisible = false
      })
    }
  }
}
</script>
