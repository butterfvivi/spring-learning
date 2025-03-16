<template>
  <el-container id="dataset_container" style="position: relative;height:100%;overflow: hidden;">
    <el-header style="height: 35px;padding: 6px 4px;background: #f2f4f7;border-bottom: 1px solid #e2e6ed;">
      <div style="position: relative;height: 24px;">
        <div class="component-icon-btn" title="添加数据库连接" @click="addDataSource('jdbc')">
          <div class="component-icon-btn-icon" style="font-size: 16px; line-height: 18px; color: rgb(61, 71, 87);">
            <i class="icons icons-16 icons-16-database" style="display: inline-block;vertical-align: text-top;font-size: 16px;color:#000000;" />
          </div>
        </div>
        <div class="component-icon-btn" title="添加SpringBean连接" @click="addDataSource('spring')">
          <div class="component-icon-btn-icon" style="font-size: 16px; line-height: 18px; color: #606266;">
            <i class="ureport ureport-leaf" style="color:#606266"></i>
          </div>
        </div>
        <div class="component-icon-btn" title="添加内置数据源连接" @click="addDataSource('buildin')">
          <div class="component-icon-btn-icon" style="font-size: 16px; line-height: 18px; color: rgb(61, 71, 87);">
            <i class="icons icons-16 icons-16-buildin" style="display: inline-block;vertical-align: text-top;font-size: 16px;color:#000000;" />
          </div>
        </div>
        <div class="component-icon-btn" title="添加API连接" @click="addDataSource('api')">
          <div class="component-icon-btn-icon" style="font-size: 16px; line-height: 18px; color: rgb(61, 71, 87);">
            <i class="el-icon-monitor"></i>
          </div>
        </div>
      </div>
    </el-header>
    <el-main style="display: flex;flex-direction: column;">
      <div class="dataset-tree-node" style="flex: 1;overflow: auto;">
        <el-scrollbar style="height: 100%">
          <div class="el-tree-node__children">
            <div v-for="(datasource, index) in datasources" class="el-tree-node is-expanded is-current is-focusable">
              <div class="el-tree-node__content" @click="handleExpand(datasource)" @contextmenu.prevent="menu($event,datasource,null,null)">
                <span :class="expand.indexOf(datasource) > -1 ? 'expanded el-tree-node__expand-icon el-icon-caret-right' : 'el-tree-node__expand-icon el-icon-caret-right' " style="padding: 6px 2px;" />
                  <i v-if="datasource.type==='jdbc'" class="icons icons-16 icons-16-database" style="display: inline-block;vertical-align: text-top;font-size: 16px;color:#000000;" />
                  <i v-else-if="datasource.type==='spring'" class="ureport ureport-leaf" style="margin-top: 4px;color:#606266;display: inline-block;vertical-align: text-top;"></i>
                  <i v-else-if="datasource.type==='buildin'" class="icons icons-16 icons-16-buildin" style="display: inline-block;vertical-align: text-top;font-size: 12px;color:#000000;" />
                  <i v-else-if="datasource.type==='api'" class="el-icon-monitor" style="display: inline-block;vertical-align: text-top;"></i>
                  <i v-else class="icons icons-16 icons-16-func_database" style="display: inline-block;vertical-align: text-top;"></i>
                <span class="el-tree-node__label" style="margin-left: 2px;">{{ datasource.name }}</span>
              </div>
              <el-collapse-transition>
                <div v-show="expand.indexOf(datasource) > -1" class="el-tree-node__children">
                  <div v-for="(dataset,index) in datasource.datasets" class="el-tree-node is-focusable">
                    <div class="el-tree-node__content" style="padding-left: 12px;" @click="handleExpand(dataset)" @contextmenu.prevent="menu($event,datasource,dataset,null)">
                      <span :class="expand.indexOf(dataset) > -1 ? 'expanded el-tree-node__expand-icon el-icon-caret-right' : 'el-tree-node__expand-icon el-icon-caret-right' "  style="padding: 6px 2px;" />
                      <span class="el-tree-node__label">{{ dataset.name }}</span>
                    </div>
                    <el-collapse-transition>
                      <div v-show="expand.indexOf(dataset) > -1" class="el-tree-node__children">
                        <div v-for="field in dataset.fields" class="el-tree-node is-focusable">
                          <div class="el-tree-node__content" style="padding-left: 28px;" @contextmenu.prevent="menu($event,datasource,dataset, field)" @dblclick="addField(dataset.name, field.name)">
                            <i class="el-icon-rank" style="color: #909399;font-size: 12px;"></i>
                            <span style="margin-left: 2px;" class="el-tree-node__label" :dataset="dataset.name" draggable="true">{{ field.name }}</span>
                          </div>
                        </div>
                      </div>
                    </el-collapse-transition>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </el-main>
    <data-source-dialog :custom-data-set-value="JdbcDataSourceValue"  />
    <buildin-data-source-dialog :buildin-data-set-value="BuildinDataSourceValue"  />
    <api-data-source-dialog :api-data-set-value="ApiDataSourceValue"  />
    <spring-bean-data-source-dialog :spring-bean-data-set-value="SpringDataSourceValue"  />

    <sql-data-set-dialog :sql-data-set-value="sqlDataSetValue" :refresh="init" />
    <spring-bean-data-set-dialog :spring-bean-data-set-value="springDataSetValue"  />
    <api-data-set-dialog :api-data-set-value="apiDataSetValue"  />
    <vue-context ref="menu">
      <li>
        <el-link v-if="current==='datasource'" :underline="false" @click.prevent="addDataSet"><i class="el-icon-plus" /> 添加数据集</el-link>
        <el-link v-if="current==='datasource'" :underline="false" @click.prevent="editDataSource"><i class="el-icon-edit" /> 编辑数据源</el-link>
        <el-link v-if="current==='datasource'" :underline="false" @click.prevent="removeDataSource"><i class="el-icon-delete" /> 删除数据源</el-link>

        <el-link v-if="current==='dataset'" :underline="false" @click.prevent="addDataField"><i class="el-icon-circle-plus-outline" /> 添加字段</el-link>
        <el-link v-if="current==='dataset'" :underline="false" @click.prevent="editDataSet"><i class="el-icon-edit" /> 编辑数据集</el-link>
        <el-link v-if="current==='dataset'" :underline="false" @click.prevent="removeDataSet"><i class="el-icon-delete" /> 删除数据集</el-link>

        <el-link v-if="current==='field'" :underline="false" @click.prevent="editDataField"><i class="el-icon-edit" /> 编辑字段</el-link>
        <el-link v-if="current==='field'" :underline="false" @click.prevent="removeDataField"><i class="el-icon-delete" /> 删除字段</el-link>
      </li>
    </vue-context>
    <el-dialog
      :title="edit? '编辑字段' : '添加字段'"
      :visible.sync="dialogVisible"
      append-to-body
      :close-on-click-modal="false"
      width="400px">
      <el-input size="small" v-model="fieldName" placeholder="字段名称"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="saveDataSetField">确 定</el-button>
      </span>
    </el-dialog>
  </el-container>
</template>

<script>

import BuildinDataSourceDialog from './dialog/BuildinDataSourceDialog'
import DataSourceDialog from './dialog/DataSourceDialog'
import ApiDataSourceDialog from './dialog/ApiDataSourceDialog'
import SpringBeanDataSourceDialog from './dialog/SpringBeanDataSourceDialog'

import SqlDataSetDialog from './dialog/SqlDataSetDialog'
import SpringBeanDataSetDialog from './dialog/SpringBeanDataSetDialog'
import ApiDataSetDialog from './dialog/ApiDataSetDialog'


import VueContext from 'vue-context'
import 'vue-context/dist/css/vue-context.css'

export default {
  name: 'DataSetProperty',
  components: {
    VueContext,
    DataSourceDialog,
    BuildinDataSourceDialog,
    ApiDataSourceDialog,
    SpringBeanDataSourceDialog,
    SpringBeanDataSetDialog,
    SqlDataSetDialog,
    ApiDataSetDialog
  },
  props: {
    dataSetValue: {
      type: Object,
      required: true
    },
    refreshChildPropertyPanel: {
      type: Function,
      required: true
    },
  },
  data: function() {
    return {
      dialogVisible: false,
      edit: false,
      fieldName: '',
      current: '',
      expand: [],
      datasources: [],
      datasets: [],
      datasource: null,
      dataset: null,
      BuildinDataSourceValue: {
        dialogVisible: false
      },
      JdbcDataSourceValue: {
        dialogVisible: false
      },
      SpringDataSourceValue: {
        dialogVisible: false
      },
      ApiDataSourceValue: {
        dialogVisible: false
      },
      sqlDataSetValue: {
        dialogVisible: false
      },
      springDataSetValue: {
        dialogVisible: false
      },
      apiDataSetValue: {
        dialogVisible: false
      },
    }
  },
  watch: {
    dataSetValue: function(val) {
      this.init(val)
    }
  },
  mounted: function() {
    this.init(this.dataSetValue)
  },
  methods: {
    init(val) {
      this.expand = []
      if (val.context && val.context.reportDef) {
        if(!val.context.reportDef.datasources) {
          val.context.reportDef.datasources = []
        }
        for(const ds of val.context.reportDef.datasources) {
          if(!ds.datasets) {
            ds.datasets = []
          }
        }
        this.datasources = val.context.reportDef.datasources
      }
    },
    addDataSource(type) {
      if(type === 'jdbc') {
        this.JdbcDataSourceValue = {
          dialogVisible: true,
          edit: false,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
        return
      }
      if(type === 'buildin') {
        this.BuildinDataSourceValue = {
          dialogVisible: true,
          context: this.dataSetValue.context
        }
        return
      }
      if(type === 'api') {
        this.ApiDataSourceValue = {
          dialogVisible: true,
          context: this.dataSetValue.context
        }
        return
      }
      if(type === 'spring') {
        this.SpringDataSourceValue = {
          dialogVisible: true,
          context: this.dataSetValue.context
        }
        return
      }
    },
    editDataSource() {
      if(this.datasource.type === 'jdbc') {
        this.JdbcDataSourceValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'api') {
        this.ApiDataSourceValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'spring') {
        this.SpringDataSourceValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      }
    },
    menu(e, datasource, dataset, field) {
      this.datasource = datasource
      this.dataset = dataset
      this.field = field
      if(this.field) {
        this.current = 'field'
      } else if(this.dataset) {
        this.current = 'dataset'
      } else {
        this.current = 'datasource'
      }
      this.$refs.menu.open(e)
    },
    handleExpand(obj) {
      const index = this.expand.indexOf(obj)
      if(index > -1) {
        this.expand.splice(index, 1)
      } else {
        this.expand.push(obj)
      }
    },
    addDataSet() {
      if(this.datasource.type === 'jdbc' || this.datasource.type === 'buildin') {
        this.sqlDataSetValue = {
          dialogVisible: true,
          edit: false,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'spring') {
        this.springDataSetValue = {
          dialogVisible: true,
          edit: false,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'api') {
        this.apiDataSetValue = {
          dialogVisible: true,
          edit: false,
          datasource: this.datasource,
          context: this.dataSetValue.context
        }
      }
    },
    editDataSet: function() {
      if(this.datasource.type === 'jdbc' || this.datasource.type === 'buildin') {
        this.sqlDataSetValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          dataset: JSON.parse(JSON.stringify(this.dataset)),
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'spring') {
        this.springDataSetValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          dataset: JSON.parse(JSON.stringify(this.dataset)),
          context: this.dataSetValue.context
        }
      } else if(this.datasource.type === 'api') {
        this.apiDataSetValue = {
          dialogVisible: true,
          edit: true,
          datasource: this.datasource,
          dataset: JSON.parse(JSON.stringify(this.dataset)),
          context: this.dataSetValue.context
        }
      }
    },
    addDataField() {
      this.edit = false
      this.fieldName = ''
      this.dialogVisible = true
    },
    editDataField() {
      this.edit = true
      this.fieldName = this.field.name
      this.dialogVisible = true
    },
    saveDataSetField() {
      if(!this.fieldName) {
        this.$message('请输入字段名称')
        return
      }
      let obj = this.dataset.fields.find(e => e.name === this.fieldName)
      if(obj && (!this.edit || this.fieldName !== this.field.name)) {
        this.$message('字段名称重复')
        return
      }
      if(this.edit) {
        let index = this.dataset.fields.indexOf(this.field)
        let field = {
          name: this.fieldName,
          type: null
        }
        this.dataset.fields.splice(index, 1, field)
      } else {
        this.dataset.fields.push({
          name: this.fieldName,
          type: null
        })
      }
      this.dialogVisible = false
    },
    removeDataSource: function() {
      this.$confirm('确定删除数据源?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const datasources = this.dataSetValue.context.reportDef.datasources || []
        const index = datasources.indexOf(this.datasource)
        if (index > -1) {
          datasources.splice(index, 1)
        }
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    removeDataSet: function() {
      this.$confirm('确定删除数据集?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const datasets = this.datasource.datasets || []
        const index = datasets.indexOf(this.dataset)
        if (index > -1) {
          datasets.splice(index, 1)
        }
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    removeDataField: function() {
      this.$confirm('确定删除数据字段?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const fields = this.dataset.fields || []
        const index = fields.indexOf(this.field)
        if (index > -1) {
          fields.splice(index, 1)
        }
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      })
    },
    addField(dataset, field) {
      const context = this.dataSetValue.context
      const selected = context.hot.getSelected()
      if(!selected) {
          return null
      }
      const startRow = selected[0]
      const startCol = selected[1]
      const endRow = selected[2]
      const endCol = selected[3]
      let cellDef = context.getCell(startRow, startCol)
      if (cellDef.value.type !== 'dataset') {
        context.removeCell(cellDef)
        cellDef = {
          value: {
            type: 'dataset',
            conditions: []
          },
          rowNumber: cellDef.rowNumber,
          columnNumber: cellDef.columnNumber,
          cellStyle: cellDef.cellStyle
        }
        context.addCell(cellDef)
      }
      cellDef.expand = 'Down'
      const value = cellDef.value
      value.aggregate = 'group'
      value.datasetName = dataset
      value.property = field
      value.order = 'none'

      let text = value.datasetName + '.' + value.aggregate + '('
      const prop = value.property
      text += prop + ')'
      context.hot.setDataAtCell(startRow, startCol, text)
      this.refreshChildPropertyPanel(startRow, startCol, endRow, endCol)
    }
  }
}
</script>

<style>
  .v-context, .v-context ul {
    border: 0px;
    -webkit-box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }

  .dataset-tree-node .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .data-source-btn {
    cursor: pointer;
    border:none;
    border-radius:0;
    padding: 6px 8px;
    background: #ffffff;
  }

  .data-source-btn:hover {
    background: #E4E7ED;
  }

  .el-tree-node__content svg {
    font-size: 14px;
  }
</style>
