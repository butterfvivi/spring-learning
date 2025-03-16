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
package org.vivi.framework.ureport.simple.ureport.export;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import org.vivi.framework.ureport.simple.ureport.build.ReportBuilder;
import org.vivi.framework.ureport.simple.ureport.build.down.DownCellbuilder;
import org.vivi.framework.ureport.simple.ureport.build.right.RightCellbuilder;
import org.vivi.framework.ureport.simple.ureport.definition.CellDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.Expand;
import org.vivi.framework.ureport.simple.ureport.definition.ReportDefinition;
import org.vivi.framework.ureport.simple.ureport.model.Report;
import org.vivi.framework.ureport.simple.ureport.parser.ReportParser;
import org.vivi.framework.ureport.simple.ureport.utils.FileUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public class ReportRender {

	private final static ReportParser reportParser = new ReportParser();

	private final static ReportBuilder reportBuilder = new ReportBuilder();

	private final static DownCellbuilder downCellParentbuilder = new DownCellbuilder();

	private final static RightCellbuilder rightCellParentbuilder = new RightCellbuilder();

	public Report render(ReportDefinition reportDefinition, Map<String, Object> parameters) {
		return reportBuilder.buildReport(reportDefinition, parameters);
	}

	/**
	 * 通过报表文件获取解析类
	 * @param file
	 * @return
	 */
	public ReportDefinition getReportDefinition(String file) {
		InputStream inputStream = FileUtils.getReport(file);
		ReportDefinition reportDefinition = reportParser.parse(inputStream, file);
		rebuildReportDefinition(reportDefinition);
		IOUtils.closeQuietly(inputStream);
		return reportDefinition;
	}
	
	/**
	 * 通过报表内容获取解析类
	 * @param file
	 * @return
	 */
	public ReportDefinition getReportDefinition(String input, String charsetName) {
		InputStream inputStream = IOUtils.toInputStream(input, charsetName);
		ReportDefinition reportDefinition = reportParser.parse(inputStream, "p");
		rebuildReportDefinition(reportDefinition);
		IOUtils.closeQuietly(inputStream);
		return reportDefinition;
	}
	
	

	private void rebuildReportDefinition(ReportDefinition reportDefinition) {
		List<CellDefinition> cells = reportDefinition.getCells();
		for (CellDefinition cell : cells) {
			addRowChildCell(cell, cell);
			addColumnChildCell(cell, cell);
		}
		for (CellDefinition cell : cells) {
			Expand expand = cell.getExpand();
			if (expand.equals(Expand.Down)) {
				downCellParentbuilder.buildParentCell(cell, cells);
			} else if (expand.equals(Expand.Right)) {
				rightCellParentbuilder.buildParentCell(cell, cells);
			}
		}
	}

	private void addRowChildCell(CellDefinition cell, CellDefinition childCell) {
		CellDefinition leftCell = cell.getLeftParentCell();
		if (leftCell == null) {
			return;
		}
		List<CellDefinition> childrenCells = leftCell.getRowChildrenCells();
		childrenCells.add(childCell);
		addRowChildCell(leftCell, childCell);
	}

	private void addColumnChildCell(CellDefinition cell, CellDefinition childCell) {
		CellDefinition topCell = cell.getTopParentCell();
		if (topCell == null) {
			return;
		}
		List<CellDefinition> childrenCells = topCell.getColumnChildrenCells();
		childrenCells.add(childCell);
		addColumnChildCell(topCell, childCell);
	}
}
