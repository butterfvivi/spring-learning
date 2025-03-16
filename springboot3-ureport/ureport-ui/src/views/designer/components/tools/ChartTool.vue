<template>
  <el-dropdown
    trigger="click"
    style="float: left;"
    @command="handleChartCell"
  >
    <span class="el-dropdown-link">
      <div class="component-icon-btn">
        <div class="component-icon-btn-icon" style="line-height: 16px;float: left;">
          <i class="icons icons-16 icons-16-bar" />
        </div>
        <div style="width: 10px;height: 24px;float: left;position: relative;">
          <div class="right-btn" />
        </div>
      </div>
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="histogram">
        <div class="chart-icon chart-icon-columnar" />
        <div class="chart-icon-label">柱状图</div>
      </el-dropdown-item>
      <el-dropdown-item command="bar">
        <div class="chart-icon chart-icon-bar" />
        <div class="chart-icon-label">条形图</div>
      </el-dropdown-item>
      <el-dropdown-item command="line">
        <div class="chart-icon chart-icon-line" />
        <div class="chart-icon-label">折线图</div>
      </el-dropdown-item>
      <el-dropdown-item command="area">
        <div class="chart-icon chart-icon-area" />
        <div class="chart-icon-label">区域图</div>
      </el-dropdown-item>
      <el-dropdown-item command="pie">
        <div style="position: absolute;margin-top: 5px;margin-left: -1px;" class="chart-icon chart-icon-pie" />
        <div style="margin-left: 29px;" class="chart-icon-label">饼状图</div>
      </el-dropdown-item>
      <el-dropdown-item command="doughnut">
        <div style="position: absolute;margin-top: 5px;margin-left: -1px;" class="chart-icon chart-icon-doughnut" />
        <div style="margin-left: 29px;" class="chart-icon-label">环形图</div>
      </el-dropdown-item>
      <el-dropdown-item command="radar">
        <div class="chart-icon chart-icon-radar" />
        <div class="chart-icon-label">雷达图</div>
      </el-dropdown-item>
      <el-dropdown-item command="scatter">
        <div class="chart-icon chart-icon-scatter" />
        <div class="chart-icon-label">散点图</div>
      </el-dropdown-item>
      <el-dropdown-item command="bubble">
        <div class="chart-icon chart-icon-bubble" />
        <div class="chart-icon-label">气泡图</div>
      </el-dropdown-item>
      <el-dropdown-item command="combo">
        <div class="chart-icon chart-icon-combo" />
        <div class="chart-icon-label">组合图</div>
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { undoManager, setDirty } from '../Utils.js'

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
    handleChartCell(val) {
      const context = this.context
      const cellDef = context.getSelectedCell()
      if (!cellDef) {
        return
      }
      const oldCellDataValue = JSON.parse(JSON.stringify(cellDef.value))
      let newCellDataValue = null
      if (cellDef.value.type === 'chart') {
        cellDef.value.chart.dataset.type = val
        cellDef.value.chart.options = null
      } else {
        newCellDataValue = {
          type: 'chart',
          chart: {
            dataset: {
              type: val
            }
          }
        }
        cellDef.value = newCellDataValue
      }
      context.refreshSelected()
      setDirty()
      undoManager.add({
        redo: function() {
          if (newCellDataValue) {
            cellDef.value = newCellDataValue
          } else {
            cellDef.value.chart.dataset.type = val
            cellDef.value.chart.options = null
          }
          context.refreshSelected()
          setDirty()
        },
        undo: function() {
          cellDef.value = oldCellDataValue
          context.refreshSelected()
          setDirty()
        }
      })
    }
  }
}
</script>
<style>
  .chart-icon {
    vertical-align: text-bottom;
    display: inline-block;
    height: 24px;
    width: 24px;
  }

  .chart-icon-label {
    display: inline-block;
    margin-left: 5px;
    color:#606266
  }

  .chart-icon-columnar {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAGlJREFUeNpi/P//PwMtAROImDVr1n+aWkBLwEKsQvVte1F8edPLmXFQ+GDwBBFOcNAFNYHY72EcWB+gJ9m0tDTGQR0HoxaMWjBqwXCwgAFUJ8+cOfM/iKYFpl9pSouKH1QSM9K62QIQYACv3F+Bi6iy1wAAAABJRU5ErkJggg==);
  }

  .chart-icon-bar {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAGlJREFUeNpi/P//PwMtAROImDVr1n+aWkBzH9ASsKALkBpcaWlpjMM7iOgfB4TCdPD7QH3bXooz3U0vZ8bRVER+HCCH39D0AcNBF/JSkf0extGyiEpxgCMsB78PaFHxg0pmRlo3WwACDAAJ4BuaCQj4CgAAAABJRU5ErkJggg==);
  }

  .chart-icon-line {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAALRJREFUeNpi/P//PwMtAROImDVr1n+aWkBzH4xaQHcL1LftFaCZBUDDE4DUA5glLDQwfD4QJ970cv5AVR+gGb4AJs5CguYJQOwA1HyBWMOJ8gGSZpCXDwD5BsQaTtACZM1ADDL4AbIlhAzHawG6ZmikOSBZ0kDIcJwW4HIZmiX1hAzHagEhbyNZEkjIcFypaAIhl0Et2UBMCmTBollgtLAbXhbAI5kWFX9aWhojI62bLQABBgAsM2k3FMWH0wAAAABJRU5ErkJggg==);
  }

  .chart-icon-area {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAIVJREFUeNpi/P//PwMtAROImDVr1n+aWkBzH4xaQFcL1LftXUAzC6CGx9PEAmyGE20BSDO614kxnCgLkDTHY7MEn+EELcCiGcUSQoaDAAsZ3gZZAmcTCgEWUsOUWINxBhEx3qY0o8WPlkXDywKMZHrTy5lxaPqAFhV/WloaIyOtmy0AAQYAfFY3VQIwcbUAAAAASUVORK5CYII=);
  }

  .chart-icon-pie {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAASlJREFUeNpi/P//PwMtARMDjQELqRrUt+11AFL7odyLQLwAhG96OX+gzAcHXRyAWABNVB+I+4H4AdDiAGzaGHHGAcSwAiBOAGJ5qKij+tdKBiQfoINEoE8WEPbBQReQoQ+AuB7JcGLAfHSfMGExvAGkEIj5yYzXBUBLBLBbAHF5PYUJhx8arGgWQMJ8ApVSZwI2HyRQECwMWFIXVgtomtH0idRzAZRcibUAkg8g4f+eCPWFUAvwO8B+zwdyi4p+ItSAfHcAMycfdKFOsWq/hxFXRrtIBeMf4ivsFlDBggWELPhIgeEfsWVUJqSw+0BhXihATj3YyyL7PRtARS4ZhjcC9S4gvj446BIADTJ+IoKlAJfhxFQ4CVCMnsvhVSW2YCHOgqHSqgAIMAC6eF3eGWOJxQAAAABJRU5ErkJggg==);
  }

  .chart-icon-doughnut {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAYtJREFUeNrUVctRwzAQtTPcUQeBE8eUYDL4xCkdECpAHWA6UCrAdOBTTmZG7iA+cgI6sCswu9HGWckf7GQ8DJpZK5Hk97xvpSe/qipvyjbzJm4XvbNZeAXPCOIWYu4FqY/DN9t3TPsbQuP8x/3dVxeE3ypRFgp4KogHa9wm4O0NQgJR8btEBlwz8BJiA7Fkq5Y0VtJ/XKuBWPRncARf0MhmL1GQFm3pEyBK+ERDOcrJM3FrEDHwRwCO+0pEQBKIdtC/0ruIIZsZmIJ+1l8epHLMbgESxTK5PhSe10AyzaMTdmTEaiLbiryiPu7SfIBcsYNlEcypT844V4mDNf1J/hMCcQae6CPI3QKd0FYOlkWg60XmRHsjz4FgBEkbgaL+kv0e0xS9W/L3Z8wp8eS91OaVheuRBAcFFPeipl1n4Y750d6GOw/e0dY1+hbIhEan+++DpqOWpClGwXYLXkJrksVYeJDqYReOIUJveR4gjdE8SKPhN5p9ZUr62oUzm1NWMdXPG0/wH6ziR4ABAKB/kfRM3EkgAAAAAElFTkSuQmCC);
  }

  .chart-icon-radar {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAtNJREFUeNq0VT9oE2EUv0vOJkIas9jBP7SQwSkQdamgcDoFB8nq0h5Usgh6WxehEVzcgluOVK7BoW43Baf2xoAIpy4iKLFqKx3i2UHT2uR8v8u78N2RkqTSBy/fl+/93u993+/edyd7niedpMUmAc+srarwEylAxOWEJNWmPWkd83Hz5FESEdkcDRb8yX7XplF9lIgjVITvLi61jn0CItdoAKlORINd81xHjDGTFaCkDLnJu8wToR3F8FoeGGCRM1YBAiLJgRMJJHAHsamdVCq+lxSKuMAEeM49+hkQAMfGkTVKdELIRjZ/6Cmvup6STMQ6qnT7kzNkYzi1SbmVUAE+Hh5ki/V2I+TQfKXTO/32oJfMpJWfs/T/MRUpR6WlAeRz3ACuL9EpSfpwveu9oy4ph8gb2Qw5tF4BYX37gb7+o2T65FhDDBhBMnCAC5xYU/BDIrULh73PqG4YBqo782c293LTsiZLXpv+X/YlMYz+JcPOG1m/dT1J3nr/8q7Z/HUzzQ+9RVx2Mx5vixLhWDbtAEnEY+S1c5UNkiRm7S58pfENt2uHvMBzNRn7c7U4U79IY8/c1m+VSiWHpcKDV4lPD7ooaEnfAJyK7dfTiusu3HuYY10hxXPyOzyvIAYMsAE5W5E5+23KHRNtMQBm0T2cDP9IvoW5v0YxH8NkguWDLhTvgc1Hk1hnAL6gZUmyoDs09gqvaT5GaFnmGFxMJbJjnds1MEuQzgxkIHJgyxyzIrsv8mbCN5mPpA6TKZd6XShdeApJVDjmWDtCHlW8pNFXRUgm49ty63c3dXAts3GJ/m6KjjXEgDlKnmEFRElg5Rc795dJY3mY+7G+VKI8YclwD0Q/a9YcjNVqVSW3o/GoAwOsmCv6sNe1c8Ws3RC6ZpT5XcU5zjjfAyvlSc+4a1qj2Bljco411ifz/Npq82//tTC20cst+X1xaX7ib/L/2j8BBgASMaNOuclTdwAAAABJRU5ErkJggg==);
  }

  .chart-icon-scatter {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAO9JREFUeNpi/P//PwMtAROImDVr1n+aWkBzH5AK1LftnQDEBsSoZSHD8AQglQ/ECkAcQHUf3PRyXgCkCoE4gSgNoFQ0c+bM/yAaGatt3TMBiA3QxUnFTDiCwQAaDBtoEsnAYLgApAKJCWNkRwHxA/TIZ8ET1qS6HhTp8tC4KSAYB9gwME4agFgAj7wBUXGAJ3nWA/EEPL6+QHYyRUqeBaSEGwuJeWAC1YoKYJAo0Kwsghp+AUgvwOMAAUp88AGIH+BL8yB5aMSTHgfAsAZZYECNIGIhRxM0OVIURMO8RiMrDmhR8aelpTEy0rrZAhBgAFCHzKWkIVXmAAAAAElFTkSuQmCC);
  }

  .chart-icon-bubble {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAASVJREFUeNpi/P//PwMtAROImDVr1n+aWkBzH5ANDroIEFLCQqbBDUCyAIj5gWyQyEUw337PAcp9cNBlAZCsBxuOAPpAvB8o50CZBQddDIBkPB4VEyj1QQABef1Bn4oeEJD/SJkF9ntAEfyQlDjAmkzVt+0VgCZDWKqYcNPLeQNSPIDY8mjaFgId0EBsPgAZYI/sdqClgWBL7PdcAPIVgCkqAUzD1EPEic5o9ljEEqAWIwcXVSNZgJqp6CIWsQXUtAAUkQeRkl4jMPxRLAAW8QJAXEBWYQc07AFSCsIAIMOBFKhg0weyDdLS0hKoXVw7IBULAUBLFKhqAdDFoNSUCA0+ByAfdw4H1ckzZ878D6JpgWle2LEgRRzVK35g0DEy0rrZAhBgADcymQAJodh2AAAAAElFTkSuQmCC);
  }

  .chart-icon-combo {
    background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAATxJREFUeNpi/P//PwMtAROI6Ozs/E9TC2gJWMjWuU0Z09dedxkHjw/EFs7FcOGr+GTGQRcHVLcAmCIVaGXBRKDhCUD6Aqmp6KPQn78x+OTf/hSImXMrLAXIzgdZRIoFG6Pffpgj8fvPFKj3v5SXlx9All90N2DO829iIHkBIHZEk8cbRIXFL95cABq+GeptEN4PtKQBJt95Oe0C0HCwfJzyBl90w3H6ABRRxQwMoPAERVhhr4QIyHUMQAtBBjQA5QMYGNIYYPLlurNA8oeAmHBGQ46oc1ycUUDDgYYx1IMw0IUTgLQDTK2x8JUooOEweaIz2nxQRAENBrl2GRDzI0sCLQFZbgAsKgKwyROTD0ARVQCk1xPQTEgeuwXYImpQF9ejFpBfHxAs+7HUXngtoEXFD0yRjIy0brYABBgAr5hy3F0dCYcAAAAASUVORK5CYII=);
  }
</style>
