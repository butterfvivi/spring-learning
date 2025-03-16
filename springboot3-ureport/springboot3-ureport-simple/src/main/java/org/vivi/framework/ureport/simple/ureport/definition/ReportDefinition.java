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
package org.vivi.framework.ureport.simple.ureport.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vivi.framework.ureport.simple.common.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.DatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.SqlDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.ApiDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.BuildinDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.JdbcDatasourceDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.Component;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.MultipleSelectInputComponent;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.RenderContext;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.SearchForm;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.SelectInputComponent;
import org.vivi.framework.ureport.simple.ureport.definition.value.DatasetValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.ExpressionValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.Value;
import org.vivi.framework.ureport.simple.ureport.export.html.SearchFormData;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Column;
import org.vivi.framework.ureport.simple.ureport.model.Report;
import org.vivi.framework.ureport.simple.ureport.model.Row;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class ReportDefinition implements Serializable {

	private static final long serialVersionUID = 5934291400824773809L;

	private String reportFullName;

	private Paper paper;

	private CellDefinition rootCell;

	private HeaderFooterDefinition header;

	private HeaderFooterDefinition footer;

	private SearchForm searchForm;

	private List<CellDefinition> cells;

	private List<RowDefinition> rows;

	private List<ColumnDefinition> columns;

	private FreezeCellDefinition freeze;

	private List<DatasourceDefinition> datasources;

	private List<FloatImage> floatImages;

	@JsonIgnore
	private String style;

	public Report newReport() {
		Report report = new Report();
		report.setReportFullName(reportFullName);
		report.setPaper(paper);
		report.setHeader(header);
		report.setFooter(footer);
		report.setFloatImages(floatImages);
		List<Row> reportRows = new ArrayList<Row>();
		List<Column> reportColumns = new ArrayList<Column>();
		report.setRows(reportRows);
		report.setColumns(reportColumns);

		Map<Integer, Row> rowMap = new HashMap<Integer, Row>();
		int headerRowsHeight = 0, footerRowsHeight = 0, titleRowsHeight = 0, summaryRowsHeight = 0;
		for (RowDefinition rowDef : rows) {
			Row newRow = rowDef.newRow(reportRows);
			newRow.setRowNumber(rowDef.getRowNumber());
			report.insertRow(newRow, rowDef.getRowNumber());
			rowMap.put(rowDef.getRowNumber(), newRow);
			Band band = rowDef.getBand();
			if (band != null) {
				if (band.equals(Band.headerrepeat)) {
					report.getHeaderRepeatRows().add(newRow);
					headerRowsHeight += newRow.getRealHeight();
				} else if (band.equals(Band.footerrepeat)) {
					report.getFooterRepeatRows().add(newRow);
					footerRowsHeight += newRow.getRealHeight();
				} else if (band.equals(Band.title)) {
					report.getTitleRows().add(newRow);
					titleRowsHeight += newRow.getRealHeight();
				} else if (band.equals(Band.summary)) {
					report.getSummaryRows().add(newRow);
					summaryRowsHeight += newRow.getRealHeight();
				}
			}
		}
		report.setRepeatHeaderRowHeight(headerRowsHeight);
		report.setRepeatFooterRowHeight(footerRowsHeight);
		report.setTitleRowsHeight(titleRowsHeight);
		report.setSummaryRowsHeight(summaryRowsHeight);
		Map<Integer, Column> columnMap = new HashMap<Integer, Column>();
		for (ColumnDefinition columnDef : columns) {
			Column newColumn = columnDef.newColumn(reportColumns);
			newColumn.setColumnNumber(columnDef.getColumnNumber());
			report.insertColumn(newColumn, columnDef.getColumnNumber());
			columnMap.put(columnDef.getColumnNumber(), newColumn);
		}
		Map<CellDefinition, Cell> cellMap = new HashMap<CellDefinition, Cell>();
		for (CellDefinition cellDef : cells) {
			Cell cell = cellDef.newCell();
			cellMap.put(cellDef, cell);
			Row targetRow = rowMap.get(cellDef.getRowNumber());
			cell.setRow(targetRow);
			targetRow.getCells().add(cell);
			Column targetColumn = columnMap.get(cellDef.getColumnNumber());
			cell.setColumn(targetColumn);
			targetColumn.getCells().add(cell);

			if (cellDef.getLeftParentCell() == null && cellDef.getTopParentCell() == null) {
				report.setRootCell(cell);
			}
			report.addCell(cell);
		}
		Map<String, CellDefinition> cellDefinitionMap = new HashMap<String, CellDefinition>();
		for (CellDefinition cellDef : cells) {
			cellDefinitionMap.put(cellDef.getName(), cellDef);
			Cell targetCell = cellMap.get(cellDef);
			CellDefinition leftParentCellDef = cellDef.getLeftParentCell();
			if (leftParentCellDef != null) {
				targetCell.setLeftParentCell(cellMap.get(leftParentCellDef));
			} else {
				targetCell.setLeftParentCell(null);
			}
			CellDefinition topParentCellDef = cellDef.getTopParentCell();
			if (topParentCellDef != null) {
				targetCell.setTopParentCell(cellMap.get(topParentCellDef));
			} else {
				targetCell.setTopParentCell(null);
			}
		}
		for (CellDefinition cellDef : cells) {
			List<String> cells = new ArrayList<String>();
			// 条件属性依赖单元格
			List<ConditionPropertyItem> conditionPropertyItems = cellDef.getConditionPropertyItems();
			if (conditionPropertyItems != null && conditionPropertyItems.size() > 0) {
				for (ConditionPropertyItem conditionPropertyItem : conditionPropertyItems) {
					List<Condition> conditions = conditionPropertyItem.getConditions();
					ToolUtils.fetchCellName(conditions, cells);
				}
			}
			Value value = cellDef.getValue();
			if (value instanceof DatasetValue) {// 数据集过滤条件依赖单元格
				DatasetValue datasetValue = (DatasetValue) value;
				List<Condition> conditions = datasetValue.getConditions();
				ToolUtils.fetchCellName(conditions, cells);
			} else if (value instanceof ExpressionValue) {// 表达式依赖单元格
				ExpressionValue expressionValue = (ExpressionValue) value;
				expressionValue.getExpression().fetchCellName(cells);
			}
			if (cells.size() > 0) {
				// 获取当前单元格与依赖的单元格父格
				for (String cellName : cells) {
					// 单元格坐标[A1:B1:C1]逐级绑定
					if(cellName.contains(":")) {
						setChildCellNames(cellName.split(":"), cellDefinitionMap, cellMap);
						continue;
					}
					CellDefinition leftParent = fetchLeftParent(cellDef, cellName);
					if (leftParent != null) {
						Cell targetCell = null;
						if(leftParent.getName().equals(cellName)) {
							CellDefinition parent = leftParent.getLeftParentCell();
							if(parent != null) {
								targetCell = cellMap.get(parent);
							}
						} else {
							targetCell = cellMap.get(leftParent);
						}
						if(targetCell != null) {
							Set<String> rowChildCellNames = targetCell.getRowChildCellNames();
							if (rowChildCellNames == null) {
								rowChildCellNames = new HashSet<String>();
								targetCell.setRowChildCellNames(rowChildCellNames);
							}
							rowChildCellNames.add(cellName);
						}
					}
					CellDefinition topParent = fetchTopParent(cellDef, cellName);
					if (topParent != null) {
						Cell targetCell = null;
						if(topParent.getName().equals(cellName)) {
							CellDefinition parent = topParent.getTopParentCell();
							if(parent != null) {
								targetCell = cellMap.get(parent);
							}
						} else {
							targetCell = cellMap.get(topParent);
						}
						if(targetCell != null) {
							Set<String> columnChildCellNames = targetCell.getColumnChildCellNames();
							if (columnChildCellNames == null) {
								columnChildCellNames = new HashSet<String>();
								targetCell.setColumnChildCellNames(columnChildCellNames);
							}
							columnChildCellNames.add(cellName);
						}
					}
				}
			}
		}
		for (CellDefinition cellDef : cells) {
			Cell targetCell = cellMap.get(cellDef);
			Cell leftParentCell = targetCell.getLeftParentCell();
			if (leftParentCell != null) {
				leftParentCell.addRowChild(targetCell);
			}
			Cell topParentCell = targetCell.getTopParentCell();
			if (topParentCell != null) {
				topParentCell.addColumnChild(targetCell);
			}
		}
		return report;
	}
	
	private void setChildCellNames(String[] cells, Map<String, CellDefinition> cellDefinitionMap, Map<CellDefinition, Cell> cellMap) {
		for (int i = cells.length - 1; i > 0; i--) {
			String name = cells[i];
			String preName = cells[i - 1];
			CellDefinition cur = cellDefinitionMap.get(name);
			CellDefinition leftParent = fetchLeftParent(cur, preName);
			if (leftParent != null && leftParent.getName().equals(preName)) {
				Cell targetCell = cellMap.get(leftParent);
				Set<String> rowChildCellNames = targetCell.getRowChildCellNames();
				if (rowChildCellNames == null) {
					rowChildCellNames = new HashSet<String>();
					targetCell.setRowChildCellNames(rowChildCellNames);
				}
				rowChildCellNames.add(name);
			}
			CellDefinition topParent = fetchTopParent(cur, preName);
			if (topParent != null && topParent.getName().equals(preName)) {
				Cell targetCell = cellMap.get(topParent);
				Set<String> columnChildCellNames = targetCell.getColumnChildCellNames();
				if (columnChildCellNames == null) {
					columnChildCellNames = new HashSet<String>();
					targetCell.setColumnChildCellNames(columnChildCellNames);
				}
				columnChildCellNames.add(name);
			}
		}
	}

	private CellDefinition fetchLeftParent(CellDefinition cellDef, String cellName) {
		CellDefinition leftParentCell = cellDef.getLeftParentCell();
		if (leftParentCell == null) {
			return null;
		}
		if(leftParentCell.getName().equals(cellName)) {
			return leftParentCell;
		}
		Set<String> set = leftParentCell.getNewCellNames();
		if (set.contains(cellName)) {
			return leftParentCell;
		}
		return fetchLeftParent(leftParentCell, cellName);
	}

	private CellDefinition fetchTopParent(CellDefinition cellDef, String cellName) {
		CellDefinition topParentCell = cellDef.getTopParentCell();
		if (topParentCell == null) {
			return null;
		}
		if(topParentCell.getName().equals(cellName)) {
			return topParentCell;
		}
		Set<String> set = topParentCell.getNewCellNames();
		if (set.contains(cellName)) {
			return topParentCell;
		}
		return fetchTopParent(topParentCell, cellName);
	}

	public String getStyle() {
		if (style == null) {
			style = buildStyle();
		}
		return style;
	}

	public void setToken(String token) {
		if (datasources != null && datasources.size() > 0) {
			for (DatasourceDefinition dsDef : datasources) {
				if (dsDef instanceof ApiDatasourceDefinition) {
					ApiDatasourceDefinition ds = (ApiDatasourceDefinition) dsDef;
					ds.setToken(token);
				}
			}
		}
	}

	private String buildStyle() {
		StringBuffer sb = new StringBuffer();
		for (CellDefinition cell : cells) {
			CellStyle cellStyle = cell.getCellStyle();
			sb.append("._" + cell.getName() + "{");
			int colWidth = getColumnWidth(cell.getColumnNumber(), cell.getColSpan());
			sb.append("width:" + colWidth + "pt;");
			Alignment align = cellStyle.getAlign();
			if (align != null) {
				sb.append("text-align:" + align.name() + ";");
			}
			Alignment valign = cellStyle.getValign();
			if (valign != null) {
				sb.append("vertical-align:" + valign.name() + ";");
			}
			float lineHeight = cellStyle.getLineHeight();
			if (lineHeight > 0) {
				sb.append("line-height:" + lineHeight + ";");
			}
			String bgcolor = cellStyle.getBgcolor();
			if (StringUtils.isNotBlank(bgcolor)) {
				sb.append("background-color:rgb(" + bgcolor + ");");
			}
			String fontFamilty = cellStyle.getFontFamily();
			if (StringUtils.isNotBlank(fontFamilty)) {
				sb.append("font-family:" + fontFamilty + ";");
			}
			int fontSize = cellStyle.getFontSize();
			sb.append("font-size:" + fontSize + "pt;");
			String foreColor = cellStyle.getForecolor();
			if (StringUtils.isNotBlank(foreColor)) {
				sb.append("color:rgb(" + foreColor + ");");
			}
			Boolean bold = cellStyle.getBold(), italic = cellStyle.getItalic(), underline = cellStyle.getUnderline();
			if (bold != null && bold) {
				sb.append("font-weight:bold;");
			}
			if (italic != null && italic) {
				sb.append("font-style:italic;");
			}
			if (underline != null && underline) {
				sb.append("text-decoration:underline;");
			}
			Border border = cellStyle.getLeftBorder();
			if (border != null) {
				sb.append("border-left:" + border.getStyle().name() + " " + border.getWidth() + "px rgb("
						+ border.getColor() + ");");
			}
			border = cellStyle.getRightBorder();
			if (border != null) {
				sb.append("border-right:" + border.getStyle().name() + " " + border.getWidth() + "px rgb("
						+ border.getColor() + ");");
			}
			border = cellStyle.getTopBorder();
			if (border != null) {
				sb.append("border-top:" + border.getStyle().name() + " " + border.getWidth() + "px rgb("
						+ border.getColor() + ");");
			}
			border = cellStyle.getBottomBorder();
			if (border != null) {
				sb.append("border-bottom:" + border.getStyle().name() + " " + border.getWidth() + "px rgb("
						+ border.getColor() + ");");
			}
			sb.append("}");
		}
		return sb.toString();
	}

	/**
	 * 表单联动刷新
	 * 
	 * @param item
	 * @param parameters 参数
	 * @return
	 */
	public Map<String, Object> refreshSearchForm(Map<String, Object> item, Map<String, Object> params) {
		// 需要查询数据的组件
		Map<String, Component> datasetComponent = new HashMap<String, Component>();
		List<Component> items = searchForm.getComponents();
		String currentUuid = StringUtils.toString(item.get("uuid"));
		if (items != null && items.size() > 0) {
			for (Component component : items) {
				String dataset = getComponentDataSetName(component);
				String uuid = component.getId();
				if (StringUtils.isNotBlank(dataset) && !currentUuid.equals(uuid)) {
					datasetComponent.put(dataset, component);
				}
			}
		}
		// 获取相关联所有数据集
		List<Dataset> dataList = new ArrayList<Dataset>();
		if (datasources != null && datasources.size() > 0) {
			String currenBindParameter = StringUtils.toString(item.get("bindParameter"));
			for (DatasourceDefinition datasourceDefinition : datasources) {
				List<DatasetDefinition> datasets = datasourceDefinition.getDatasets();
				if (datasets != null && datasets.size() > 0) {
					List<DatasetDefinition> newDatasetDefinition = new ArrayList<DatasetDefinition>();
					if (datasourceDefinition instanceof JdbcDatasourceDefinition) {
						JdbcDatasourceDefinition datasource = (JdbcDatasourceDefinition) datasourceDefinition;
						for (DatasetDefinition datasetDefinition : datasets) {
							SqlDatasetDefinition sqlDatasetDefinition = (SqlDatasetDefinition) datasetDefinition;
							if (datasetComponent.get(datasetDefinition.getName()) != null
									&& sqlDatasetDefinition.hasParameter(currenBindParameter)) {
								newDatasetDefinition.add(datasetDefinition);
							}
						}
						// 重新设置需要查询的数据集
						datasource.setDatasets(newDatasetDefinition);
						dataList.addAll(datasource.buildDatasets(params));
					} else if (datasourceDefinition instanceof BuildinDatasourceDefinition) {
						BuildinDatasourceDefinition datasource = (BuildinDatasourceDefinition) datasourceDefinition;
						for (DatasetDefinition datasetDefinition : datasets) {
							SqlDatasetDefinition sqlDatasetDefinition = (SqlDatasetDefinition) datasetDefinition;
							if (datasetComponent.get(datasetDefinition.getName()) != null
									&& sqlDatasetDefinition.hasParameter(currenBindParameter)) {
								newDatasetDefinition.add(datasetDefinition);
							}
						}
						// 重新设置需要查询的数据集
						datasource.setDatasets(newDatasetDefinition);
						dataList.addAll(datasource.buildDatasets(params));
					}
				}
			}
		}
		// 刷新相关联表单数据
		Map<String, Object> result = new HashMap<String, Object>();
		for (Dataset dataset : dataList) {
			String name = dataset.getName();
			Component component = datasetComponent.get(name);
			if (component != null) {
				String uuid = component.getId();
				result.put(uuid, searchForm.refersh(component, dataset));
			}
		}
		return result;
	}

	/**
	 * 获取组件数据集名称
	 * 
	 * @param component 组件信息
	 * @return 数据集名称
	 */
	private String getComponentDataSetName(Component component) {
		if (component instanceof SelectInputComponent) {
			SelectInputComponent selectInputComponent = (SelectInputComponent) component;
			return selectInputComponent.getDataset();

		} else if (component instanceof MultipleSelectInputComponent) {
			MultipleSelectInputComponent multipleSelectInputComponent = (MultipleSelectInputComponent) component;
			return multipleSelectInputComponent.getDataset();
		}
		return null;
	}

	public SearchFormData buildSearchFormData(Map<String, Dataset> datasetMap, Map<String, Object> parameters) {
		if (searchForm == null) {
			return null;
		}
		Map<String, Object> defaultParameters = getDefaultParameters(parameters);
		if (parameters != null) {
			defaultParameters.putAll(parameters);
		}
		RenderContext context = new RenderContext(datasetMap, defaultParameters);
		SearchFormData data = searchForm.builder(context);
		return data;
	}

	private Map<String, Object> getDefaultParameters(Map<String, Object> parameters) {
		Map<String, Object> defaultParameters = new HashMap<String, Object>();
		if (datasources != null && datasources.size() > 0) {
			for (DatasourceDefinition datasourceDefinition : datasources) {
				List<DatasetDefinition> datasets = datasourceDefinition.getDatasets();
				if (datasets != null && datasets.size() > 0) {
					for (DatasetDefinition datasetDefinition : datasets) {
						if (datasetDefinition instanceof SqlDatasetDefinition) {
							SqlDatasetDefinition sqlDatasetDefinition = (SqlDatasetDefinition) datasetDefinition;
							Map<String, Object> param = sqlDatasetDefinition.buildParameters(parameters);
							defaultParameters.putAll(param);
						}
					}
				}
			}
		}
		return defaultParameters;
	}

	private int getColumnWidth(int columnNumber, int colSpan) {
		int width = 0;
		if (colSpan > 0)
			colSpan--;
		int start = columnNumber, end = start + colSpan;
		for (int i = start; i <= end; i++) {
			for (ColumnDefinition col : columns) {
				if (col.getColumnNumber() == i) {
					width += col.getWidth();
				}
			}
		}
		return width;
	}

	public String getReportFullName() {
		return reportFullName;
	}

	public void setReportFullName(String reportFullName) {
		this.reportFullName = reportFullName;
	}

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}

	public CellDefinition getRootCell() {
		return rootCell;
	}

	public void setRootCell(CellDefinition rootCell) {
		this.rootCell = rootCell;
	}

	public HeaderFooterDefinition getHeader() {
		return header;
	}

	public void setHeader(HeaderFooterDefinition header) {
		this.header = header;
	}

	public HeaderFooterDefinition getFooter() {
		return footer;
	}

	public void setFooter(HeaderFooterDefinition footer) {
		this.footer = footer;
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(SearchForm searchForm) {
		this.searchForm = searchForm;
	}

	public List<RowDefinition> getRows() {
		return rows;
	}

	public void setRows(List<RowDefinition> rows) {
		this.rows = rows;
	}

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
	}

	public List<CellDefinition> getCells() {
		return cells;
	}

	public void setCells(List<CellDefinition> cells) {
		this.cells = cells;
	}

	public void setDatasources(List<DatasourceDefinition> datasources) {
		this.datasources = datasources;
	}

	public List<DatasourceDefinition> getDatasources() {
		return datasources;
	}

	public FreezeCellDefinition getFreeze() {
		return freeze;
	}

	public void setFreeze(FreezeCellDefinition freeze) {
		this.freeze = freeze;
	}

	public List<FloatImage> getFloatImages() {
		return floatImages;
	}

	public void setFloatImages(List<FloatImage> floatImages) {
		this.floatImages = floatImages;
	}
}
