/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.simple.ureport.chart.dataset.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.CollectData;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.CollectType;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.DataSet;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.Dict;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.Series;
import org.vivi.framework.ureport.simple.ureport.definition.value.ChartValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.Value;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.DataUtils;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2017年6月9日
 */
public abstract class CategoryDataSet implements DataSet {
	
	@JsonIgnore
	private LinkedHashSet<Object> seriesCategorys;

	/** 数据集 **/
	private String datasetName;

	/** 分类属性  **/
	private String categoryProperty;
	
	/** 分类属性格式化  **/
	private String format;

	/** 分类属性字典映射  **/
	private Dict dict;
	
	/** 静态系列属性  **/
	private List<Series> series;

	/** 动态系列属性  **/
	private String seriesProperty;

	/** 动态系列属性字典映射  **/
	private Dict seriesDict;
	
	/** 动态系列属性格式化  **/
	private String seriesFormat;

	public String buildDataSetJson(Context context, Cell cell, String props) {
		// 分类属性字典映射
		if (dict != null) {
			dict.build(context);
		}
		// 动态系列属性字典映射
		if (seriesDict != null) {
			seriesDict.build(context);
		}
		// 动态系列
		if (StringUtils.isNotBlank(seriesProperty)) {
			seriesCategorys = new LinkedHashSet<Object>();
		}
		List<?> dataList = DataUtils.fetchData(cell, context, datasetName);
		Map<Object, Map<Object, CollectData>> cacheData = new LinkedHashMap<Object, Map<Object, CollectData>>();
		Condition condition = getCondition(cell);
		for (Object obj : dataList) {
			Object category = ToolUtils.getProperty(obj, categoryProperty);
			if (category == null) {
				continue;
			}
			// 过滤条件
			if (condition != null && !condition.filter(cell, cell, obj, context)) {
				continue;
			}
			// 格式化分类
			category = formatValue(category, format);
			Map<Object, CollectData> row = cacheData.get(category);
			if (row == null) {
				row = new HashMap<Object, CollectData>();
				cacheData.put(category, row);
			}
			// 动态系列
			if (StringUtils.isNotBlank(seriesProperty)) {
				// 系列分类
				Object seriesCategory = ToolUtils.getProperty(obj, seriesProperty);
				if (seriesCategory == null) {
					continue;
				}
				// 格式化系列分类
				seriesCategory = formatValue(seriesCategory, seriesFormat);
				Series s = series.get(0);
				String seriesProperty = s.getSeriesProperty();
				Object value = ToolUtils.getProperty(obj, seriesProperty);
				if (value == null) {
					continue;
				}
				CollectData data = row.get(seriesCategory);
				if(data == null) {
					data = new CollectData();
					row.put(seriesCategory, data);
					seriesCategorys.add(seriesCategory);
				}
				collectData(data, s, value);
			} else {
				for (Series s : series) {
					String property = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, property);
					if (value == null) {
						continue;
					}
					CollectData data = row.get(s);
					if(data == null) {
						data = new CollectData();
						row.put(s, data);
					}
					collectData(data, s, value);
				}
			}
		}
		return buildDataSets(cacheData, props);
	}
	
	public void collectData(CollectData data, Series s, Object value) {
		int count = data.getCount();
		double val = data.getValue();
		double cur = ToolUtils.toBigDecimal(value).doubleValue();
		CollectType collectType = s.getCollectType();
		if(collectType == CollectType.select && count == 0) {
			data.setValue(cur);
		} else if(collectType == CollectType.avg) {
			data.setValue(val + cur);
		} else if(collectType == CollectType.max) {
			data.setValue(Math.max(val, cur));
		} else if(collectType == CollectType.min) {
			data.setValue(Math.min(val, cur));
		} else if(collectType == CollectType.sum) {
			data.setValue(val + cur);
		}
		data.setCount(count + 1);
	}
	
	public double collectData(CollectData data, CollectType collectType) {
		double value = data.getValue();
		int count = data.getCount();
		if(collectType == CollectType.avg) {
			value = ToolUtils.toBigDecimal(value).divide(ToolUtils.toBigDecimal(count), 8, BigDecimal.ROUND_HALF_UP).doubleValue();
		} else if(collectType == CollectType.count) {
			value = count;
		}
		return value;
	}
	
	private String buildDataSets(Map<Object, Map<Object, CollectData>> map, String props) {
		StringBuilder sb = new StringBuilder();
		Series firstSeries = series.get(0);
		for (Map.Entry<Object, Map<Object, CollectData>> entry : map.entrySet()) {
			Object category = entry.getKey();
			sb.append("[");
			if (dict != null && dict.isDict()) {
				sb.append("\"" + dict.getValue(String.valueOf(category)) + "\",");
			} else {
				sb.append("\"" + category + "\",");
			}
			Map<Object, CollectData> list = entry.getValue();
			if (StringUtils.isNotBlank(seriesProperty)) {
				CollectType collectType = firstSeries.getCollectType();
				for (Object key : seriesCategorys) {
					Integer decimal = firstSeries.getFormat();
					CollectData data = list.get(key);
					double value = 0;
					if(data != null) {
						value = collectData(data, collectType);
					}
					sb.append(ToolUtils.format(value, decimal)).append(",");
				}
			} else {
				for (Series s : series) {
					Integer decimal = s.getFormat();
					CollectType collectType = s.getCollectType();
					CollectData data = list.get(s);
					double value = 0;
					if(data != null) {
						value = collectData(data, collectType);
					}
					sb.append(ToolUtils.format(value, decimal)).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("],");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	public String getLabels() {
		StringBuilder sb = new StringBuilder("\"category\",");
		if (StringUtils.isNotBlank(seriesProperty) && seriesCategorys != null) {
			for (Object obj : seriesCategorys) {
				if (seriesDict != null && seriesDict.isDict()) {
					sb.append("\"" + seriesDict.getValue(String.valueOf(obj)) + "\",");
				} else {
					sb.append("\"" + obj + "\",");
				}
			}
		} else {
			for (Series s : series) {
				sb.append("\"" + s.getSeriesText() + "\",");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public Object formatValue(Object value, String format) {
		return ToolUtils.format(value, format);
	}

	public Condition getCondition(Cell cell) {
		Value value = cell.getValue();
		Condition condition = null;
		if (value instanceof ChartValue) {
			ChartValue chartValue = (ChartValue) value;
			condition = chartValue.getCondition();
		}
		return condition;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getCategoryProperty() {
		return categoryProperty;
	}

	public void setCategoryProperty(String categoryProperty) {
		this.categoryProperty = categoryProperty;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	public Dict getSeriesDict() {
		return seriesDict;
	}

	public void setSeriesDict(Dict seriesDict) {
		this.seriesDict = seriesDict;
	}

	public String getSeriesProperty() {
		return seriesProperty;
	}

	public void setSeriesProperty(String seriesProperty) {
		this.seriesProperty = seriesProperty;
	}

	public String getSeriesFormat() {
		return seriesFormat;
	}

	public void setSeriesFormat(String seriesFormat) {
		this.seriesFormat = seriesFormat;
	}
}
