<template>
  <div class="component-icon-btn" @click="clearStyle">
    <div class="component-icon-btn-icon">
      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16"><g fill="#3D4757" fill-rule="evenodd"><path d="M8.213 13H6.8l6.636-6.636-4.243-4.243-7.07 7.071L5.928 13H4.515L1.06 9.546a.5.5 0 010-.707L8.839 1.06a.5.5 0 01.707 0l4.95 4.95a.5.5 0 010 .707L8.213 13z" fill-rule="nonzero"/><path d="M4.536 6.364l4.95 4.95-.707.707-4.95-4.95zM4.521 13h10.03v1H5.496z"/></g></svg>
    </div>
  </div>
</template>
<script>

import { doCleanCell } from '../table/operation/CleanCellOperation.js'


export default {
  props: {
    context: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      
    }
  },
  mounted: function() {

  },
  methods: {
    clearStyle() {
      doCleanCell.call(this.context.hot,'style')
    },
  }
}
</script>
