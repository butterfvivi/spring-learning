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

/**
 * @author Jacky.gao
 * @since 2017年6月8日
 */
public class PieDataSet extends CategoryDataSet {
	
	@Override
	public String buildDataJson(Context context, Cell cell) {
		String datasetJson = buildDataSetJson(context, cell, null);
		StringBuilder sb = new StringBuilder();
		sb.append("\"dataset\":[" + datasetJson + "],");
		sb.append("\"dimensions\":[\"name\",\"value\"],");
		return sb.toString();
	}
	
	@Override
	public String getType() {
		return "pie";
	}
	
	@Override
	public String buildDataSetJson(Context context, Cell cell, String props) {
		String categoryProperty = super.getCategoryProperty();
		// 分类属性字典映射
		Dict categoryDict = getDict();
		if (categoryDict != null) {
			categoryDict.build(context);
		}
		String datasetName = super.getDatasetName();
		List<?> dataList = DataUtils.fetchData(cell, context, datasetName);
		Map<Object,CollectData> seriesDataMap = new LinkedHashMap<Object,CollectData>();
		
		List<Series> series = super.getSeries();
		Condition condition = getCondition(cell);
		String format = getFormat();
		for (Object obj : dataList) {
			// 过滤条件
			if (condition != null && !condition.filter(cell, cell, obj, context)) {
				continue;
			}
			//一个分类一个指标构成饼图
			if (StringUtils.isNotBlank(categoryProperty)) {
				Object category = ToolUtils.getProperty(obj, categoryProperty);
				if (category == null) {
					continue;
				}
				//格式化分类
				category = formatValue(category, format);
				if(series != null && series.size() >0) {
					Series s = series.get(0);
					String seriesProperty = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, seriesProperty);
					if (value == null) {
						continue;
					}
					CollectData data = seriesDataMap.get(category);
					if(data == null) {
						data = new CollectData();
						seriesDataMap.put(category, data);
					}
					collectData(data, s, value);
				}
				
			} else {//没有分类，多个指标构成饼图
				for (Series s : series) {
					String seriesProperty = s.getSeriesProperty();
					Object value = ToolUtils.getProperty(obj, seriesProperty);
					if (value == null) {
						continue;
					}
					CollectData data = seriesDataMap.get(s);
					if(data == null) {
						data = new CollectData();
						seriesDataMap.put(s, data);
					}
					collectData(data, s, value);
				}
			}
		}
		return buildDataSets(seriesDataMap, props);
	}

	
	private String buildDataSets(Map<Object,CollectData> map, String props) {
		StringBuilder sb = new StringBuilder();
		Dict categoryDict = super.getDict();
		String categoryProperty = super.getCategoryProperty();
		List<Series> series = super.getSeries();
		//一个分类一个指标构成饼图
		if (StringUtils.isNotBlank(categoryProperty)) {
			Series s = series.get(0);
			Integer decimal = s.getFormat();
			for (Map.Entry<Object, CollectData> entry : map.entrySet()) {
				Object obj = entry.getKey();
				CollectData data = entry.getValue();
				double value = collectData(data, s.getCollectType());
				if (categoryDict != null && categoryDict.isDict()) {
					sb.append("[\"").append(categoryDict.getValue(String.valueOf(obj))).append("\",").append(ToolUtils.format(value, decimal)).append("],");
				} else {
					sb.append("[\"").append(obj).append("\",").append(ToolUtils.format(value, decimal)).append("],");
				}
			}
		} else {
			for (Map.Entry<Object, CollectData> entry : map.entrySet()) {
				Series s = (Series) entry.getKey();
				Integer decimal = s.getFormat();
				CollectData data = map.get(s);
				double value = 0;
				if(data != null) {
					value = collectData(data, s.getCollectType());
				}
				String text = s.getSeriesText();
				sb.append("[\"").append(text).append("\",").append(ToolUtils.format(value, decimal)).append("],");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
