package org.vivi.framework.ureport.simple.ureport.chart.dataset.impl;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.chart.dataset.Series;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

public class ComboDataSet extends CategoryDataSet{

	@Override
	public String buildDataJson(Context context, Cell cell) {
		// 数据集
		String datasetJson = buildDataSetJson(context, cell, null);
		StringBuilder sb = new StringBuilder();
		sb.append("\"dataset\":[" + datasetJson + "],");
		// 维度
		String labels = super.getLabels();
		sb.append("\"dimensions\":[" + labels + "],");
		// 系列
		String seriesJson = getSeriesJson();
		sb.append("\"series\":[" + seriesJson + "],");
		return sb.toString();
	}
	
	@Override
	public String getType() {
		return "combo";
	}
	
	private String getSeriesJson() {
		List<Series> series = super.getSeries();
		StringBuilder sb= new StringBuilder();
		for (Series s : series) {
			String type = s.getType();
			Boolean yAxisIndex = s.getyAxisIndex();
			sb.append("{\"type\":\"").append(type).append("\",\"yAxisIndex\":").append(yAxisIndex).append("},");
		}
		if (series.size() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
}
