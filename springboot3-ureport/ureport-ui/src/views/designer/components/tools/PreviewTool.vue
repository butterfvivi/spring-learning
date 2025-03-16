<template>
  <div class="component-icon-btn" @click="preview">
    <div class="component-icon-btn-icon" style="font-size: 16px;line-height: 18px;color:#3D4757">
      <i class="el-icon-view"></i>
    </div>
  </div>
</template>

<script>
import {
  tableToXml
} from '../Utils.js'

export default {
  name: 'PreviewTool',
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data() {
    return {

    }
  },
  mounted() {

  },
  methods: {
    async preview() {
      const content = await tableToXml(this.context)
      const param = {
        content: content
      }
      var { href } = this.$router.resolve({
        path: '/ureport/preview'
      })
      window.localStorage.setItem('reportParameters', JSON.stringify(param))
      window.open(href)
    }
  }
}
</script>

<style>
</style>
