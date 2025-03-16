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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
public class RadarDataSet extends CategoryDataSet {

	@JsonIgnore
	private LinkedHashSet<Object> seriesCategorys;
	
	@Override
	public String buildDataJson(Context context, Cell cell) {
		String datasetJson = buildDataSetJson(context, cell, null);
		StringBuilder sb = new StringBuilder();
		sb.append("\"dataset\":[" + datasetJson + "],");
		String labels = getLabels();
		sb.append("\"dimensions\":[" + labels + "],");
		return sb.toString();
	}

	@Override
	public String getType() {
		return "radar";
	}

	@Override
	public String buildDataSetJson(Context context, Cell cell, String props) {
		// 分类属性
		String categoryProperty = super.getCategoryProperty();
		if(StringUtils.isNotBlank(categoryProperty)) {
			return super.buildDataSetJson(context, cell, props);
		}
		Dict seriesDict = super.getSeriesDict();
		// 动态系列属性字典映射
		if (seriesDict != null) {
			seriesDict.build(context);
		}
		// 系列属性
		String seriesProperty = super.getSeriesProperty();
		String seriesFormat = super.getSeriesFormat();
		if (StringUtils.isNotBlank(seriesProperty)) {
			seriesCategorys = new LinkedHashSet<Object>();
		}
		String datasetName = super.getDatasetName();
		List<?> dataList = DataUtils.fetchData(cell, context, datasetName);
		Map<Series, Map<Object, CollectData>> cacheData = new LinkedHashMap<Series, Map<Object, CollectData>>();
		Map<Series,CollectData> seriesDataMap = new LinkedHashMap<Series, CollectData>();
		List<Series> series = super.getSeries();
		Condition condition = getCondition(cell);
		for (Object obj : dataList) {
			// 过滤条件
			if (condition != null && !condition.filter(cell, cell, obj, context)) {
				continue;
			}
			if (StringUtils.isNotBlank(seriesProperty)) {
				Object seriesCategory = ToolUtils.getProperty(obj, seriesProperty);
				if (seriesCategory == null) {
					continue;
				}
				// 格式化系列分类
				seriesCategory = formatValue(seriesCategory, seriesFormat);
				for (Series s : series) {
					String property = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, property);
					if (value == null) {
						continue;
					}
					Map<Object, CollectData> row = cacheData.get(s);
					if (row == null) {
						row = new LinkedHashMap<Object, CollectData>();
						cacheData.put(s, row);
					}
					seriesCategorys.add(seriesCategory);
					CollectData data = row.get(seriesCategory);
					if (data == null) {
						data = new CollectData();
						row.put(seriesCategory, data);
					}
					super.collectData(data, s, value);
				}
			} else {
				for (Series s : series) {
					String property = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, property);
					if (value == null) {
						continue;
					}
					CollectData data = seriesDataMap.get(s);
					if (data == null) {
						data = new CollectData();
						seriesDataMap.put(s, data);
					}
					super.collectData(data, s, value);
				}
			}
		}
		if (StringUtils.isNotBlank(seriesProperty)) {
			return buildSeriesPropertyDataSets(cacheData, props);
		} else {
			return buildDataSets(seriesDataMap, props);
		}
	}
	
	@Override
	public String getLabels() {
		StringBuilder sb = new StringBuilder("\"category\",");
		String categoryProperty = super.getCategoryProperty();
		Dict seriesDict = super.getSeriesDict();
		// 系列属性
		String seriesProperty = super.getSeriesProperty();
		if (StringUtils.isNotBlank(categoryProperty)) {
			return super.getLabels();
		} else if (StringUtils.isNotBlank(seriesProperty) && seriesCategorys != null) {
			for (Object obj : seriesCategorys) {
				if (seriesDict != null && seriesDict.isDict()) {
					sb.append("\"" + seriesDict.getValue(String.valueOf(obj)) + "\",");
				} else {
					sb.append("\"" + obj + "\",");
				}
			}
		}else {
			sb.append("\"系列值\",");
		}
		sb.deleteCharAt(sb.length()-1); // 删除最后一个逗号
		return sb.toString();
	}
	
	private String buildSeriesPropertyDataSets(Map<Series, Map<Object, CollectData>> map, String props) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Series, Map<Object, CollectData>> entry : map.entrySet()) {
			Series s = entry.getKey();
			String text = s.getSeriesText();
			Integer decimal = s.getFormat();
			sb.append("[\"").append(text).append("\",");
			Map<Object, CollectData> list = entry.getValue();
			for (Map.Entry<Object, CollectData> e : list.entrySet()) {
				CollectData data = e.getValue();
				double value = collectData(data, s.getCollectType());
				sb.append(ToolUtils.format(value, decimal)).append(",");
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("],");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	private String buildDataSets(Map<Series,CollectData> map, String props) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Series, CollectData> entry : map.entrySet()) {
			Series s = entry.getKey();
			Integer decimal = s.getFormat();
			CollectData data = entry.getValue();
			double value = super.collectData(data,s.getCollectType());
			String text = s.getSeriesText();
			sb.append("[\"").append(text).append("\",").append(ToolUtils.format(value, decimal)).append("],");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
