/**
 * Created by Jacky.Gao on 2017-06-22.
 */
import uuid from 'node-uuid'
import * as echarts from 'echarts'
import * as chart from './Chart'

export default function renderChart(ele,cellDef) {
  const config = cellDef.value.chart
  if(!config.options) {
    return
  }
  const data = mock(cellDef.value.chart)
  if(data) {
    const option = chart.builder(data)
    const myChart = echarts.init(ele)
    myChart.setOption(option)
  }
}

function mock(chart) {
  const dataset = chart.dataset || {}
  // 指标系列
  const series = dataset.series || []
  // 柱状图、条形图、折线图、面积图、组合图 必须有分类和至少一个系列值
  if (dataset.type === 'histogram' || dataset.type === 'bar' || dataset.type === 'line' || dataset.type === 'area' || dataset.type === 'combo') {
    if(series.length === 0 || !dataset.categoryProperty) {
      return null
    }
  }
  // 饼图、环形图、雷达图必须至少一个系列值
  if (dataset.type === 'pie' || dataset.type === 'doughnut' || dataset.type === 'radar') {
    if(series.length === 0) {
      return null
    }
  }
  // 散点图必须有分类和至少两个系列值
  if (dataset.type === 'scatter' || dataset.type === 'bubble') {
    if(series.length < 2 || !dataset.categoryProperty) {
      return null
    }
  }
  // 气泡图必须有分类和至少三个系列值
  if (dataset.type === 'bubble') {
    if(series.length < 3 || !dataset.categoryProperty) {
      return null
    }
  }
  const data = {
    type: dataset.type,
    dimensions: [],
    dataset: [],
    series: series,
    options: chart.options
  }
  if(dataset.categoryProperty) { //分类
    data.dimensions.push('category')
    if(dataset.seriesProperty) { //系列
      for(var j = 1; j <= 2; j++) {
        data.dimensions.push('系列' + j)
      }
    } else {
      for(const i in series) {
        const item = series[i]
        data.dimensions.push(item.seriesText)
      }
    }
    for(var i = 1; i <= 5; i++) {
      const row = []
      row.push("分类"+ i)
      if(dataset.seriesProperty) { //系列
        for(var j = 1; j <= 2; j++) {
          row.push(parseInt(Math.random()*10000) + 1000)
        }
      } else {
        for(var j = 0; j < series.length; j++) {
          row.push(parseInt(Math.random()*10000) + 1000)
        }
      }
      data.dataset.push(row)
    }
    if(dataset.type === 'scatter' || dataset.type === 'bubble') {
      data.dataset = []
      data.dimensions = []
      data.indicators = []
      for(const item of series) {
        data.indicators.push(item.seriesText)
      }
      data.dimensions.push('category')
      if(dataset.seriesProperty) { //系列
        for(var k = 1; k <= 2; k++) {
          data.dimensions.push('系列'+ k)
          let source = []
          for(var i = 1; i <= 5; i++) {
            let row = []
            row.push("分类"+ i)
            for(const item of series) {
              row.push(parseInt(Math.random()*10000) + 1000)
            }
            source.push(row)
          }
          data.dataset.push({
            source: source
          })
        }
      } else {
        let source = []
        for(var i = 1; i <= 5; i++) {
          let row = []
          row.push("分类"+ i)
          for(const item of series) {
            row.push(parseInt(Math.random()*10000) + 1000)
          }
          source.push(row)
        }
        data.dataset.push({
          source: source
        })
      }
    }
  } else {
    data.dimensions.push('x')
    data.dimensions.push('y')
    if(dataset.seriesProperty) { //系列
      for(var j = 1; j <= 2; j++) {
        const row = []
        row.push('系列' + j)
        row.push(parseInt(Math.random()*10000) + 1000)
        data.dataset.push(row)
      }
    } else {
      for(const item of series) {
        const row = []
        row.push(item.seriesText)
        row.push(parseInt(Math.random()*10000) + 1000)
        data.dataset.push(row)
      }
    }
  }
  return data
}
