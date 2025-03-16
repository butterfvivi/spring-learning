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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.vivi.framework.ureport.simple.common.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.CollectData;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.Dict;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.Series;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.DataUtils;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2017年6月8日
 */
public class ScatterDataSet extends CategoryDataSet {
	
	@JsonIgnore
	private LinkedHashSet<Object> seriesCategorys;
	
	@Override
	public String buildDataJson(Context context, Cell cell) {
		String datasetJson = buildDataSetJson(context, cell, null);
		StringBuilder sb = new StringBuilder();
		sb.append("\"dataset\":[" + datasetJson + "],");
		String labels = getLabels();
		sb.append("\"dimensions\":[" + labels + "],");
		String indicators = getIndicators();
		sb.append("\"indicators\":[" + indicators + "],");
		return sb.toString();
	}
	
	@Override
	public String buildDataSetJson(Context context, Cell cell, String props) {
		String categoryProperty = super.getCategoryProperty();
		Dict dict = super.getDict();
		// 分类属性字典映射
		if (dict != null) {
			dict.build(context);
		}
		Dict seriesDict = super.getSeriesDict();
		String format = super.getFormat();
		// 动态系列属性字典映射
		if (seriesDict != null) {
			seriesDict.build(context);
		}
		String datasetName = super.getDatasetName();
		List<?> dataList = DataUtils.fetchData(cell, context, datasetName);
		Condition condition = getCondition(cell);
		List<Series> series = super.getSeries();
		// 动态系列
		String seriesProperty = super.getSeriesProperty();
		String seriesFormat = super.getSeriesFormat();
		if (StringUtils.isNotBlank(seriesProperty)) {
			seriesCategorys = new LinkedHashSet<Object>();
			Map<Object,Map<Object,List<CollectData>>> seriesDataMap = new LinkedHashMap<Object,Map<Object,List<CollectData>>>();
			for (Object obj : dataList) {
				// 过滤条件
				if (condition != null && !condition.filter(cell, cell, obj, context)) {
					continue;
				}
				Object category = ToolUtils.getProperty(obj, categoryProperty);
				if (category == null) {
					continue;
				}
				category = formatValue(category, format);
				// 系列分类
				Object seriesCategory = ToolUtils.getProperty(obj, seriesProperty);
				if (seriesCategory == null) {
					continue;
				}
				seriesCategory = formatValue(seriesCategory, seriesFormat);
				Map<Object,List<CollectData>> seriesMap = seriesDataMap.get(seriesCategory);
				if(seriesMap == null) {
					seriesCategorys.add(seriesCategory);
					seriesMap = new LinkedHashMap<Object,List<CollectData>>();
					seriesDataMap.put(seriesCategory, seriesMap);
				}
				List<CollectData> rows = seriesMap.get(category);
				if(rows == null) {
					rows = new ArrayList<CollectData>();
					seriesMap.put(category, rows);
				}
				//第一个为X轴第二个为Y轴第三个为球体大小
				for (int i = 0; i < series.size() && i < 3; i++) {
					Series s = series.get(i);
					String property = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, property);
					if (value == null) {
						continue;
					}
					if(rows.size() < i + 1) {
						rows.add(new CollectData());
					}
					CollectData data = rows.get(i);
					collectData(data, s, value);
				}
			}
			return buildSeriesDataSets(seriesDataMap, props);
			
		} else {
			Map<Object,List<CollectData>> seriesDataMap = new LinkedHashMap<Object,List<CollectData>>();
			for (Object obj : dataList) {
				Object category = ToolUtils.getProperty(obj, categoryProperty);
				if (category == null) {
					continue;
				}
				// 过滤条件
				if (condition != null && !condition.filter(cell, cell, obj, context)) {
					continue;
				}
				//格式化分类
				category = formatValue(category, format);
				List<CollectData> rows = seriesDataMap.get(category);
				if(rows == null) {
					rows = new ArrayList<CollectData>();
					seriesDataMap.put(category, rows);
				}
				//第一个为X轴第二个为Y轴第三个为球体大小
				for (int i = 0; i < series.size() && i < 3; i++) {
					Series s = series.get(i);
					String property = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, property);
					if (value == null) {
						continue;
					}
					if(rows.size() < i + 1) {
						rows.add(new CollectData());
					}
					CollectData data = rows.get(i);
					collectData(data, s, value);
				}
			}
			return buildDataSets(seriesDataMap, props);
		}
	}
	
	@Override
	public String getLabels() {
		StringBuilder sb = new StringBuilder("\"category\",");
		Dict seriesDict = super.getSeriesDict();
		String seriesProperty = super.getSeriesProperty();
		if (StringUtils.isNotBlank(seriesProperty) && seriesCategorys != null) {
			for (Object obj : seriesCategorys) {
				if (seriesDict != null && seriesDict.isDict()) {
					sb.append("\"").append(seriesDict.getValue(String.valueOf(obj))).append("\",");
				} else {
					sb.append("\"" + obj + "\",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	private String getIndicators() {
		List<Series> series = super.getSeries();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < series.size() && i < 3; i++) {
			Series s = series.get(i);
			sb.append("\"" + s.getSeriesText() + "\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	private String buildSeriesDataSets(Map<Object,Map<Object,List<CollectData>>> map, String props) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : map.keySet()) {
			String dataset = buildDataSets(map.get(obj), props);
			sb.append(dataset).append(",");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private String buildDataSets(Map<Object,List<CollectData>> map, String props) {
		List<Series> series = super.getSeries();
		StringBuilder sb = new StringBuilder();
		Dict categoryDict = super.getDict();
		sb.append("{\"source\":[");
//		sb.append("[");
//		sb.append("\"category\",");
//		for (int i = 0; i < series.size() && i < 3; i++) {
//			Series s = series.get(i);
//			String seriesText = s.getSeriesText();
//			sb.append("\"").append(seriesText).append("\",");
//		}
//		sb.deleteCharAt(sb.length()-1);
//		sb.append("],");
		for (Object obj : map.keySet()) {
			sb.append("[");
			if (categoryDict != null && categoryDict.isDict()) {
				sb.append("\"").append(categoryDict.getValue(String.valueOf(obj))).append("\",");
			} else {
				sb.append("\"").append(obj).append("\",");
			}
			List<CollectData> list = map.get(obj);
			for (int i = 0; i < series.size() && i < 3; i++) {
				Series s = series.get(i);
				CollectData data = list.get(i);
				double value = collectData(data,s.getCollectType());
				Integer decimal = s.getFormat();
				sb.append(ToolUtils.format(value, decimal)).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("],");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]}");
		return sb.toString();
	}
	
	@Override
	public String getType() {
		return "scatter";
	}
}
