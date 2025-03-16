
export const fontFamilys = [
  { label: '宋体', value: '宋体' },
  { label: '仿宋', value: '仿宋' },
  { label: '黑体', value: '黑体' },
  { label: '楷体', value: '楷体' },
  { label: '微软雅黑', value: '微软雅黑' },
  { label: 'Arial', value: 'Arial' },
  { label: 'Impact', value: 'Impact' },
  { label: 'Times new Roman', value: 'Times new Roman' },
  { label: 'Comic Sans MS', value: 'Comic Sans MS' },
  { label: 'Courier New', value: 'Courier New' }
]

export const fontWeights = [
  { value: 'lighter', label: '细' },
  { value: 'normal', label: '正常' },
  { value: 'bolder', label: '粗' },
  { value: '100', label: '100' },
  { value: '200', label: '200' },
  { value: '300', label: '300' },
  { value: '400', label: '400' },
  { value: '500', label: '500' },
  { value: '600', label: '600' },
  { value: '700', label: '700' },
  { value: '800', label: '800' },
  { value: '900', label: '900' },
]

export const fontSizes = [7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 22, 24, 26, 36, 42]

export const groups = [
  { label: '列表', value: 'select' },
  { label: '分组', value: 'group' },
  { label: '自定义分组', value: 'customgroup' },
  { label: '汇总', value: 'sum' },
  { label: '统计数量', value: 'count' },
  { label: '最大值', value: 'max' },
  { label: '最小值', value: 'min' },
  { label: '平均值', value: 'avg' }
]

export const orders = [
  { label: '不排序', value: 'none' },
  { label: '正序', value: 'asc' },
  { label: '倒序', value: 'desc' }
]

export const expands = [
  { label: '向下', value: 'Down' },
  { label: '向右', value: 'Right' },
  { label: '不展开', value: 'None' },
]

export const valueFormats = [
  { value: 'yyyy' },
  { value: 'yyyy-MM' },
  { value: 'yyyy-MM-dd' },
  { value: 'yyyy-MM-dd HH:mm:ss' },
  { value: 'yyyy/MM' },
  { value: 'yyyy/MM/dd' },
  { value: 'yyyy年MM月dd日' },
  { value: 'yyyy年MM月dd日 HH:mm:ss' },
  { value: 'HH:mm' },
  { value: 'HH:mm:ss' },
  { value: '#.##' },
  { value: '#.00' },
  { value: '##.##%' },
  { value: '##.00%' },
  { value: '##,###.##' },
  { value: '￥##,###.##' },
  { value: '$##,###.##' },
  { value: '0.00E00' },
  { value: '##0.0E0' }
]

export const joinTypes = [
  { label: '且', value: 'and' },
  { label: '或', value: 'or' }
]

export const symbolTypes = [
  { label: '大于', value: '>' },
  { label: '大于等于', value: '>=' },
  { label: '小于', value: '<' },
  { label: '小于等于', value: '<=' },
  { label: '等于', value: '==' },
  { label: '不等于', value: '!=' },
  { label: '在集合中', value: 'in' },
  { label: '不在集合中', value: 'not in' },
  { label: '相似', value: 'like' }
]

export const scopeTypes = [
  { label: '当前单元格', value: 'cell' },
  { label: '当前行', value: 'row' },
  { label: '当前列', value: 'column' }
]

export const alignTypes = [
  { label: '居左', value: 'left' },
  { label: '居中', value: 'center' },
  { label: '居右', value: 'right' }
]

export const valignTypes = [
  { label: '顶部对齐', value: 'top' },
  { label: '中部对齐', value: 'middle' },
  { label: '底部对齐', value: 'bottom' }
]

export const booleanTypes = [
  { label: '是', value: 'true' },
  { label: '否', value: 'false' }
]

export const orients = [
  { value: 'horizontal', label: '水平' },
  { value: 'vertical', label: '垂直' },
]

export const legendLocations = [
  { value: 'top-left', label: '顶部居左' },
  { value: 'top-center', label: '顶部居中' },
  { value: 'top-right', label: '顶部居右' },
  { value: 'middle-left', label: '左侧居中' },
  { value: 'middle-right', label: '右侧居中' },
  { value: 'bottom-left', label: '底部居左' },
  { value: 'bottom-center', label: '底部居中' },
  { value: 'bottom-right', label: '底部居右' },
]

export const titleLocations = [
  { value: 'start', label: '开头' },
  { value: 'center', label: '中间' },
  { value: 'end', label: '末尾' },
]

export const lineStyles = [
  { value: 'solid', label: '实线' },
  { value: 'dashed', label: '虚线' },
]

export const echartsLablePositions = [
  { value: 'top', label: '顶部' },
  { value: 'left', label: '左侧' },
  { value: 'right', label: '右侧' },
  { value: 'bottom', label: '底部' },
  { value: 'inside', label: '内部' },
  { value: 'insideLeft', label: '内-左侧' },
  { value: 'insideRight', label: '内-右侧' },
  { value: 'insideTop', label: '内-顶部' },
  { value: 'insideBottom', label: '内-底部' },
  { value: 'insideTopLeft', label: '内-顶部居左' },
  { value: 'insideBottomLeft', label: '内-底部居左' },
  { value: 'insideTopRight', label: '内-顶部居右' },
  { value: 'insideBottomRight', label: '内-顶部居右' },
]

export const dateTypes = [
  { label: 'yyyy', value: 'year' },
  { label: 'yyyy-MM', value: 'month' },
  { label: 'yyyy-MM-dd', value: 'date' },
  { label: 'yyyy-MM-dd HH:mm:ss', value: 'datetime' },
]

export const colors = [
  ['rgb(255, 255, 255)', 'rgb(0, 1, 0)', 'rgb(231, 229, 230)', 'rgb(68, 85, 105)', 'rgb(91, 156, 214)',
    'rgb(237, 125, 49)', 'rgb(165, 165, 165)', 'rgb(255, 192, 1)', 'rgb(67, 113, 198)', 'rgb(113, 174, 71)'
  ],
  ['rgb(242, 242, 242)', 'rgb(127, 127, 127)', 'rgb(208, 206, 207)', 'rgb(213, 220, 228)',
    'rgb(222, 234, 246)', 'rgb(252, 229, 213)', 'rgb(237, 237, 237)', 'rgb(255, 242, 205)',
    'rgb(217, 226, 243)', 'rgb(227, 239, 217)'
  ],
  ['rgb(216, 216, 216)', 'rgb(89, 89, 89)', 'rgb(175, 171, 172)', 'rgb(173, 184, 202)',
    'rgb(189, 215, 238)', 'rgb(247, 204, 172)', 'rgb(219, 219, 219)', 'rgb(255, 229, 154)',
    'rgb(179, 198, 231)', 'rgb(197, 224, 179)'
  ],
  ['rgb(191, 191, 191)', 'rgb(63, 63, 63)', 'rgb(117, 111, 111)', 'rgb(133, 150, 176)',
    'rgb(156, 194, 230)', 'rgb(244, 177, 132)', 'rgb(201, 201, 201)', 'rgb(254, 217, 100)',
    'rgb(142, 170, 218)', 'rgb(167, 208, 140)'
  ],
  ['rgb(165, 165, 165)', 'rgb(38, 38, 38)', 'rgb(58, 56, 57)', 'rgb(51, 63, 79)', 'rgb(46, 117, 181)',
    'rgb(196, 90, 16)', 'rgb(123, 123, 123)', 'rgb(191, 142, 1)', 'rgb(47, 85, 150)', 'rgb(83, 129, 54)'
  ],
  ['rgb(127, 127, 127)', 'rgb(12, 12, 12)', 'rgb(23, 21, 22)', 'rgb(34, 42, 53)', 'rgb(31, 78, 122)',
    'rgb(132, 60, 10)', 'rgb(82, 82, 82)', 'rgb(126, 96, 0)', 'rgb(32, 56, 100)', 'rgb(54, 86, 36)'
  ],
  ['rgb(192, 0, 0)', 'rgb(254, 0, 0)', 'rgb(253, 193, 1)', 'rgb(255, 255, 1)', 'rgb(147, 208, 81)',
    'rgb(0, 176, 78)', 'rgb(1, 176, 241)', 'rgb(1, 112, 193)', 'rgb(1, 32, 96)', 'rgb(112, 48, 160)'
  ]
]
