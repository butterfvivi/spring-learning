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
package org.vivi.framework.ureport.simple.ureport.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.vivi.framework.ureport.simple.ureport.definition.CellDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.ColumnDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.FloatImage;
import org.vivi.framework.ureport.simple.ureport.definition.ReportDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.RowDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportParseException;
import org.vivi.framework.ureport.simple.ureport.parser.impl.CellParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.ColumnParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.DatasourceParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.FloatImageParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.FooterParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.FreezeCellParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.HeaderParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.PaperParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.RowParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.searchform.SearchFormParser;

/**
 * @author Jacky.gao
 * @since 2016年12月2日
 */
public class ReportParser {

	public ReportDefinition parse(InputStream inputStream, String file) {
		ReportDefinition report = new ReportDefinition();
		report.setReportFullName(file);
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(inputStream);
			Element element = document.getRootElement();
			if (!element.getName().equals("ureport")) {
				throw new ReportParseException("Unknow report file.");
			}
			List<RowDefinition> rows = new ArrayList<RowDefinition>();
			List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
			List<CellDefinition> cells = new ArrayList<CellDefinition>();
			List<DatasourceDefinition> datasources = new ArrayList<DatasourceDefinition>();
			List<FloatImage> floatImages = new ArrayList<FloatImage>();
			report.setRows(rows);
			report.setColumns(columns);
			report.setCells(cells);
			report.setDatasources(datasources);
			report.setFloatImages(floatImages);
			for (Object obj : element.elements()) {
				if (obj == null || !(obj instanceof Element)) {
					continue;
				}
				Element ele = (Element) obj;
				String eleName = ele.getName();
				if (RowParser.instance.support(eleName)) {
					rows.add(RowParser.instance.parse(ele));
				} else if (ColumnParser.instance.support(eleName)) {
					columns.add(ColumnParser.instance.parse(ele));
				} else if (CellParser.instance.support(eleName)) {
					cells.add(CellParser.instance.parse(ele));
				} else if (DatasourceParser.instance.support(eleName)) {
					datasources.add(DatasourceParser.instance.parse(ele));
				} else if (PaperParser.instance.support(eleName)) {
					report.setPaper(PaperParser.instance.parse(ele));
				} else if (HeaderParser.instance.support(eleName)) {
					report.setHeader(HeaderParser.instance.parse(ele));
				} else if (FooterParser.instance.support(eleName)) {
					report.setFooter(FooterParser.instance.parse(ele));
				} else if (FreezeCellParser.instance.support(eleName)) {
					report.setFreeze(FreezeCellParser.instance.parse(ele));
				} else if (SearchFormParser.instance.support(eleName)) {
					report.setSearchForm(SearchFormParser.instance.parse(ele));
				} else if (FloatImageParser.instance.support(eleName)) {
					floatImages.add(FloatImageParser.instance.parse(ele));
				} else {
					throw new ReportParseException("Unknow element :" + ele.getName());
				}
			}
			Collections.sort(rows);
			Collections.sort(columns);
		} catch (Exception ex) {
			throw new ReportParseException(ex);
		}
		rebuild(report);
		return report;
	}

	private void rebuild(ReportDefinition report) {
		List<CellDefinition> cells = report.getCells();
		Map<String, CellDefinition> cellsMap = new HashMap<String, CellDefinition>();
		Map<String, CellDefinition> cellsRowColMap = new HashMap<String, CellDefinition>();
		for (CellDefinition cell : cells) {
			cellsMap.put(cell.getName(), cell);
			int rowNum = cell.getRowNumber();
			int colNum = cell.getColumnNumber();
			int rowSpan = cell.getRowSpan();
			int colSpan = cell.getColSpan();
			rowSpan = rowSpan > 0 ? rowSpan-- : 1;
			colSpan = colSpan > 0 ? colSpan-- : 1;
			int rowStart = rowNum;
			int rowEnd = rowNum + rowSpan;
			int colStart = colNum;
			int colEnd = colNum + colSpan;
			for (int i = rowStart; i < rowEnd; i++) {
				for (int j = colStart; j < colEnd; j++) {
					cellsRowColMap.put(i + "," + j, cell);
				}
			}
		}
		for (CellDefinition cell : cells) {
			int rowNumber = cell.getRowNumber();
			int colNumber = cell.getColumnNumber();
			// 左父格
			String leftParentCellName = cell.getLeftParentCellName();
			if (StringUtils.isNotBlank(leftParentCellName)) {
				if (!leftParentCellName.equals("root")) {
					CellDefinition targetCell = cellsMap.get(leftParentCellName);
					if (targetCell == null) {
						throw new ReportException("Cell [" + cell.getName() + "] 's left parent cell ["
								+ leftParentCellName + "] not exist.");
					}
					cell.setLeftParentCell(targetCell);
				}
			} else {
				if (colNumber > 1) {
					CellDefinition targetCell = cellsRowColMap.get(rowNumber + "," + (colNumber - 1));
					cell.setLeftParentCell(targetCell);
				}
			}
			// 上父格
			String topParentCellName = cell.getTopParentCellName();
			if (StringUtils.isNotBlank(topParentCellName)) {
				if (!topParentCellName.equals("root")) {
					CellDefinition targetCell = cellsMap.get(topParentCellName);
					if (targetCell == null) {
						throw new ReportException("Cell [" + cell.getName() + "] 's top parent cell ["
								+ topParentCellName + "] not exist.");
					}
					cell.setTopParentCell(targetCell);
				}
			} else {
				if (rowNumber > 1) {
					CellDefinition targetCell = cellsRowColMap.get((rowNumber - 1) + "," + colNumber);
					cell.setTopParentCell(targetCell);
				}
			}
		}
	}
}
