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
package org.vivi.framework.ureport.simple.ureport.model;

import java.awt.Font;
import java.awt.FontMetrics;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.build.Range;
import org.vivi.framework.ureport.simple.ureport.definition.Alignment;
import org.vivi.framework.ureport.simple.ureport.definition.BlankCellInfo;
import org.vivi.framework.ureport.simple.ureport.definition.Border;
import org.vivi.framework.ureport.simple.ureport.definition.CellStyle;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionCellStyle;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionPaging;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionPropertyItem;
import org.vivi.framework.ureport.simple.ureport.definition.Expand;
import org.vivi.framework.ureport.simple.ureport.definition.LinkParameter;
import org.vivi.framework.ureport.simple.ureport.definition.PagingPosition;
import org.vivi.framework.ureport.simple.ureport.definition.Scope;
import org.vivi.framework.ureport.simple.ureport.definition.value.SimpleValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.Value;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class Cell implements ReportCell {
	
	private String name;
	
	private int rowSpan;
	
	private int colSpan;

	/**
	 * 下面属性用于存放分页后的rowspan信息
	 */
	private int pageRowSpan = -1;

	private String renderBean;

	/**
	 * 当前单元格计算后的实际值
	 */
	private Object data;

	/**
	 * 存储当前单元格对应值在进行格式化后的值
	 */
	private Object formatData;

	private CellStyle cellStyle;
	
	private CellStyle customCellStyle;
	
	private Value value;
	
	private Row row;
	
	private Column column;
	
	private Expand expand;
	
	private boolean processed;
	
	private boolean blankCell;
	
	private boolean existPageFunction;
	
	private List<Object> bindData;
	
	private Range duplicateRange;
	
	private boolean forPaging;
	
	private String linkUrl;
	
	private String linkTargetWindow;
	
	private List<LinkParameter> linkParameters;

	private Map<String, String> linkParameterMap;

	private Expression linkUrlExpression;

	private List<ConditionPropertyItem> conditionPropertyItems;

	private boolean fillBlankRows;
	
	/**
	 * 允许填充空白行时fillBlankRows=true，要求当前数据行数必须是multiple定义的行数的倍数，否则就补充空白行
	 */
	private int multiple;

	/**
	 * 当前单元格左父格
	 */
	private Cell leftParentCell;
	
	/**
	 * 当前单元格上父格
	 */
	private Cell topParentCell;

	private Set<String> increaseSpanCellNames;

	private Map<String, BlankCellInfo> newBlankCellsMap;

	private Set<String> newCellNames;
	
	private Set<String> rowChildCellNames;
	
	private Set<String> columnChildCellNames;
	
	private Map<String,List<Cell>> rowChildrenCells;

	private Map<String,List<Cell>> columnChildrenCells;

	public Cell newRowBlankCell(Context context, BlankCellInfo blankCellInfo, ReportCell mainCell) {
		Cell blankCell = newCell();
		blankCell.setBlankCell(true);
		blankCell.setValue(new SimpleValue(""));
		blankCell.setExpand(Expand.None);
		blankCell.setBindData(null);
		if (blankCellInfo != null) {
			int offset = blankCellInfo.getOffset();
			int mainRowNumber = mainCell.getRow().getRowNumber();
			if (offset == 0) {
				blankCell.setRow(mainCell.getRow());
			} else {
				int rowNumber = mainRowNumber + offset;
				Row row = context.getRow(rowNumber);
				blankCell.setRow(row);
			}
			blankCell.setRowSpan(blankCellInfo.getSpan());
		}
		return blankCell;
	}

	public Cell newColumnBlankCell(Context context, BlankCellInfo blankCellInfo, ReportCell mainCell) {
		Cell blankCell = newCell();
		blankCell.setBlankCell(true);
		blankCell.setValue(new SimpleValue(""));
		blankCell.setExpand(Expand.None);
		blankCell.setBindData(null);

		int offset = blankCellInfo.getOffset();
		int mainColNumber = mainCell.getColumn().getColumnNumber();
		if (offset == 0) {
			blankCell.setColumn(mainCell.getColumn());
		} else {
			int colNumber = mainColNumber + offset;
			Column col = context.getColumn(colNumber);
			blankCell.setColumn(col);
		}
		blankCell.setColSpan(blankCellInfo.getSpan());
		return blankCell;
	}

	public Cell newCell() {
		Cell cell = new Cell();
		cell.setColumn(column);
		cell.setRow(row);
		cell.setLeftParentCell(leftParentCell);
		cell.setTopParentCell(topParentCell);
		cell.setValue(value);
		cell.setRowSpan(rowSpan);
		cell.setColSpan(colSpan);
		cell.setExpand(expand);
		cell.setName(name);
		cell.setCellStyle(cellStyle);
		cell.setNewBlankCellsMap(newBlankCellsMap);
		cell.setNewCellNames(newCellNames);
		cell.setRowChildCellNames(rowChildCellNames);
		cell.setColumnChildCellNames(columnChildCellNames);
		cell.setIncreaseSpanCellNames(increaseSpanCellNames);
		cell.setDuplicateRange(duplicateRange);
		cell.setLinkParameters(linkParameters);
		cell.setLinkTargetWindow(linkTargetWindow);
		cell.setLinkUrl(linkUrl);
		cell.setPageRowSpan(pageRowSpan);
		cell.setConditionPropertyItems(conditionPropertyItems);
		cell.setFillBlankRows(fillBlankRows);
		cell.setMultiple(multiple);
		cell.setLinkUrlExpression(linkUrlExpression);
		return cell;
	}

	@Override
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getFormatData() {
		if (formatData == null) {
			return data;
		}
		return formatData;
	}

	public void setFormatData(Object formatData) {
		this.formatData = formatData;
	}

	public void doCompute(Context context) {
		doComputeConditionProperty(context);
		doFormat();
		doDataWrapCompute(context);
	}

	public void addRowChild(Cell child) {
		String name = child.getName();
		if(rowChildCellNames != null && rowChildCellNames.contains(name)) {
			if (rowChildrenCells == null) {
				rowChildrenCells = new HashMap<String, List<Cell>>();
			}
			List<Cell> list = rowChildrenCells.get(name);
			if (list == null) {
				list = new ArrayList<Cell>();
				rowChildrenCells.put(name, list);
			}
			list.add(child);
		}
		if(leftParentCell != null) {
			leftParentCell.addRowChild(child);
		}
	}

	public void addColumnChild(Cell child) {
		String name = child.getName();
		if(columnChildCellNames != null && columnChildCellNames.contains(name)) {
			if (columnChildrenCells == null) {
				columnChildrenCells = new HashMap<String, List<Cell>>();
			}
			List<Cell> list = columnChildrenCells.get(name);
			if (list == null) {
				list = new ArrayList<Cell>();
				columnChildrenCells.put(name, list);
			}
			list.add(child);
		}
		if(topParentCell != null) {
			topParentCell.addColumnChild(child);
		}
	}
	
	public Set<String> getRowChildCellNames() {
		return rowChildCellNames;
	}

	public void setRowChildCellNames(Set<String> rowChildCellNames) {
		this.rowChildCellNames = rowChildCellNames;
	}

	public Set<String> getColumnChildCellNames() {
		return columnChildCellNames;
	}

	public void setColumnChildCellNames(Set<String> columnChildCellNames) {
		this.columnChildCellNames = columnChildCellNames;
	}

	public List<Cell> getRowChildrenCells(String cellName) {
		if(rowChildrenCells == null) {
			return null;
		}
		return rowChildrenCells.get(cellName);
	}

	public List<Cell> getColumnChildrenCells(String cellName) {
		if(columnChildrenCells == null) {
			return null;
		}
		return columnChildrenCells.get(cellName);
	}

	public void doFormat() {
		String format = cellStyle.getFormat();
		if (customCellStyle != null) {
			String customFormat = customCellStyle.getFormat();
			if (StringUtils.isNotBlank(customFormat)) {
				format = customFormat;
			}
		}
		if (StringUtils.isNotBlank(format)) {
			formatData = ToolUtils.format(data, format);
		}
	}

	private void doComputeConditionProperty(Context context) {
		if (conditionPropertyItems == null || conditionPropertyItems.size() == 0) {
			return;
		}
		for (ConditionPropertyItem item : conditionPropertyItems) {
			Condition condition = item.getCondition();
			if (condition == null) {
				continue;
			}
			Object obj = null;
			List<Object> bindDataList = this.bindData;
			if (bindDataList != null && bindDataList.size() > 0) {
				obj = bindDataList.get(0);
			}
			if (!condition.filter(this, this, obj, context)) {
				continue;
			}
			ConditionPaging paging = item.getPaging();
			if (paging != null) {
				PagingPosition position = paging.getPosition();
				if (position != null) {
					if (position.equals(PagingPosition.after)) {
						int line = paging.getLine();
						if (line == 0) {
							row.setPageBreak(true);
						} else {
							int rowNumber = row.getRowNumber() + line;
							Row targetRow = context.getRow(rowNumber);
							targetRow.setPageBreak(true);
						}

					} else {
						int rowNumber = row.getRowNumber() - 1;
						Row targetRow = context.getRow(rowNumber);
						if (targetRow != null) {
							targetRow.setPageBreak(true);
						}
					}
				}
			}

			int rowHeight = item.getRowHeight();
			if (rowHeight > -1) {
				row.setRealHeight(rowHeight);
				if (rowHeight == 0 && !row.isHide()) {
					context.doHideProcessRow(row);
				}
			}
			int colWidth = item.getColWidth();
			if (colWidth > -1) {
				column.setWidth(colWidth);
				if (colWidth == 0 && !column.isHide()) {
					context.doHideProcessColumn(column);
				}
			}
			if (StringUtils.isNotBlank(item.getNewValue())) {
				this.data = item.getNewValue();
				this.formatData = item.getNewValue();
			}
			if (StringUtils.isNotBlank(item.getLinkUrl())) {
				linkUrl = item.getLinkUrl();
				if (StringUtils.isNotBlank(item.getLinkTargetWindow())) {
					linkTargetWindow = item.getLinkTargetWindow();
				}
				if (item.getLinkParameters() != null && item.getLinkParameters().size() > 0) {
					linkParameters = item.getLinkParameters();
				}
			}
			ConditionCellStyle style = item.getCellStyle();
			if (style != null) {
				Boolean bold = style.getBold();
				if (bold != null) {
					Scope scope = style.getBoldScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setBold(bold);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setBold(bold);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setBold(bold);
					}
				}
				Boolean italic = style.getItalic();
				if (italic != null) {
					Scope scope = style.getItalicScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setItalic(italic);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setItalic(italic);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setItalic(italic);
					}
				}
				Boolean underline = style.getUnderline();
				if (underline != null) {
					Scope scope = style.getUnderlineScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setUnderline(underline);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setUnderline(underline);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setUnderline(underline);
					}

				}
				String forecolor = style.getForecolor();
				if (StringUtils.isNotBlank(forecolor)) {
					Scope scope = style.getForecolorScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setForecolor(forecolor);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setForecolor(forecolor);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setForecolor(forecolor);
					}
				}
				String bgcolor = style.getBgcolor();
				if (StringUtils.isNotBlank(bgcolor)) {
					Scope scope = style.getBgcolorScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setBgcolor(bgcolor);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setBgcolor(bgcolor);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setBgcolor(bgcolor);
					}
				}
				int fontSize = style.getFontSize();
				if (fontSize > 0) {
					Scope scope = style.getFontSizeScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setFontSize(fontSize);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setFontSize(fontSize);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setFontSize(fontSize);
					}
				}
				String fontFamily = style.getFontFamily();
				if (StringUtils.isNotBlank(fontFamily)) {
					Scope scope = style.getFontFamilyScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setFontFamily(fontFamily);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setFontFamily(fontFamily);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setFontFamily(fontFamily);
					}
				}
				String format = style.getFormat();
				if (StringUtils.isNotBlank(format)) {
					if (this.customCellStyle == null) {
						this.customCellStyle = new CellStyle();
					}
					this.customCellStyle.setFormat(format);
				}
				Alignment align = style.getAlign();
				if (align != null) {
					Scope scope = style.getAlignScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setAlign(align);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setAlign(align);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setAlign(align);
					}
				}
				Alignment valign = style.getValign();
				if (valign != null) {
					Scope scope = style.getValignScope();
					if (scope.equals(Scope.cell)) {
						if (this.customCellStyle == null) {
							this.customCellStyle = new CellStyle();
						}
						this.customCellStyle.setValign(valign);
					} else if (scope.equals(Scope.row)) {
						if (row.getCustomCellStyle() == null) {
							row.setCustomCellStyle(new CellStyle());
						}
						row.getCustomCellStyle().setValign(valign);
					} else if (scope.equals(Scope.column)) {
						if (column.getCustomCellStyle() == null) {
							column.setCustomCellStyle(new CellStyle());
						}
						column.getCustomCellStyle().setValign(valign);
					}
				}
				Border leftBorder = style.getLeftBorder();
				if (leftBorder != null) {
					this.customCellStyle.setLeftBorder(leftBorder);
				}
				Border rightBorder = style.getRightBorder();
				if (rightBorder != null) {
					this.customCellStyle.setRightBorder(rightBorder);
				}
				Border topBorder = style.getTopBorder();
				if (topBorder != null) {
					this.customCellStyle.setTopBorder(topBorder);
				}
				Border bottomBorder = style.getBottomBorder();
				if (bottomBorder != null) {
					this.customCellStyle.setBottomBorder(bottomBorder);
				}
			}
		}
	}

	public void doDataWrapCompute(Context context) {
//		Boolean wrapCompute = cellStyle.getWrapCompute();
//		if (wrapCompute == null || !wrapCompute) {
//			return;
//		}
//		Object targetData = getFormatData();
//		if (targetData == null || !(targetData instanceof String)) {
//			return;
//		}
//		String dataText = targetData.toString();
//		if (StringUtils.isBlank(dataText) || dataText.length() < 2) {
//			return;
//		}
//		int totalColumnWidth = column.getWidth();
//		if (colSpan > 0) {
//			int colNumber = column.getColumnNumber();
//			for (int i = 1; i < colSpan; i++) {
//				Column col = context.getColumn(colNumber + i);
//				totalColumnWidth += col.getWidth();
//			}
//		}
//		Font font = cellStyle.getFont();
//		JLabel jLabel = new JLabel();
//		FontMetrics fontMetrics = jLabel.getFontMetrics(font);
//		int textWidth = fontMetrics.stringWidth(dataText);
//
//		double fontSize = cellStyle.getFontSize();
//		float lineHeight = 1.2f;
//		if (cellStyle.getLineHeight() > 0) {
//			lineHeight = cellStyle.getLineHeight();
//		}
//		fontSize = fontSize * lineHeight;
//		int singleLineHeight = UnitUtils.pointToPixel(fontSize) - 2;
//		if (textWidth <= totalColumnWidth) {
//			return;
//		}
//		int totalLineHeight = singleLineHeight;
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < dataText.length(); i++) {
//			char text = dataText.charAt(i);
//			sb.append(text);
//			int width = fontMetrics.stringWidth(sb.toString());
//			if (width >= totalColumnWidth) {
//				totalLineHeight += singleLineHeight;
//				sb.delete(0, sb.length());
//				sb.append(text);
//			}
//		}
//		int totalRowHeight = row.getHeight();
//		if (rowSpan > 0) {
//			int rowNumber = row.getRowNumber();
//			for (int i = 1; i < rowSpan; i++) {
//				Row targetRow = context.getRow(rowNumber + i);
//				totalRowHeight += targetRow.getHeight();
//			}
//		}
//		int dif = totalLineHeight - totalRowHeight;
//		if (dif > 0) {
//			int rowHeight = row.getHeight();
//			int newRowHeight = rowHeight + dif;
//			if (row.getRealHeight() < newRowHeight) {
//				row.setRealHeight(newRowHeight);
//			}
//		}
	}

	public static void main(String[] args) {

		FontMetrics fontMetrics = new JLabel().getFontMetrics(new Font("宋体", Font.PLAIN, 12));
		String text = "312132312312323";
		int columnWidth = 80;
		long start = System.currentTimeMillis();
		int totalLineHeight = 0;
		int singleLineHeight = fontMetrics.getHeight();
		StringBuffer multipleLine = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char str = text.charAt(i);
			if (str == '9') {
				System.out.println("aaa");
			}
			sb.append(str);
			int width = fontMetrics.stringWidth(sb.toString());
			if (width >= columnWidth) {
				sb.deleteCharAt(sb.length() - 1);
				if (multipleLine.length() > 0) {
					multipleLine.append("\r");
					totalLineHeight += singleLineHeight;
				}
				multipleLine.append(sb);
				sb.delete(0, sb.length());
				sb.append(str);
			}
		}
		if (multipleLine.length() > 0) {
			multipleLine.append("\r");
		}
		if (sb.length() > 0) {
			multipleLine.append(sb);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println(multipleLine.toString());
		System.out.println("totalLineHeight:" + totalLineHeight);
	}

	@Override
	public CellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public CellStyle getCustomCellStyle() {
		return customCellStyle;
	}

	public void setCustomCellStyle(CellStyle customCellStyle) {
		this.customCellStyle = customCellStyle;
	}

	public boolean isBlankCell() {
		return blankCell;
	}

	public void setBlankCell(boolean blankCell) {
		this.blankCell = blankCell;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	@Override
	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getPageRowSpan() {
		if (pageRowSpan == -1) {
			return rowSpan;
		}
		return pageRowSpan;
	}

	public void setPageRowSpan(int pageRowSpan) {
		this.pageRowSpan = pageRowSpan;
	}

	@Override
	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	@Override
	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	@Override
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public String getRenderBean() {
		return renderBean;
	}

	public void setRenderBean(String renderBean) {
		this.renderBean = renderBean;
	}

	public void setForPaging(boolean forPaging) {
		this.forPaging = forPaging;
	}

	public boolean isForPaging() {
		return forPaging;
	}

	public Range getDuplicateRange() {
		return duplicateRange;
	}

	public void setDuplicateRange(Range duplicateRange) {
		this.duplicateRange = duplicateRange;
	}

	public List<ConditionPropertyItem> getConditionPropertyItems() {
		return conditionPropertyItems;
	}

	public void setConditionPropertyItems(List<ConditionPropertyItem> conditionPropertyItems) {
		this.conditionPropertyItems = conditionPropertyItems;
	}

	@Override
	public Expand getExpand() {
		return expand;
	}

	public void setExpand(Expand expand) {
		this.expand = expand;
	}

	public Cell getLeftParentCell() {
		return leftParentCell;
	}

	public void setLeftParentCell(Cell leftParentCell) {
		this.leftParentCell = leftParentCell;
	}

	public Cell getTopParentCell() {
		return topParentCell;
	}

	public void setTopParentCell(Cell topParentCell) {
		this.topParentCell = topParentCell;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public boolean isExistPageFunction() {
		return existPageFunction;
	}

	public void setExistPageFunction(boolean existPageFunction) {
		this.existPageFunction = existPageFunction;
	}

	@Override
	public List<Object> getBindData() {
		return bindData;
	}

	public void setBindData(List<Object> bindData) {
		this.bindData = bindData;
	}

	public Set<String> getIncreaseSpanCellNames() {
		return increaseSpanCellNames;
	}

	public void setIncreaseSpanCellNames(Set<String> increaseSpanCellNames) {
		this.increaseSpanCellNames = increaseSpanCellNames;
	}

	public Map<String, BlankCellInfo> getNewBlankCellsMap() {
		return newBlankCellsMap;
	}

	public void setNewBlankCellsMap(Map<String, BlankCellInfo> newBlankCellsMap) {
		this.newBlankCellsMap = newBlankCellsMap;
	}

	public Set<String> getNewCellNames() {
		return newCellNames;
	}

	public void setNewCellNames(Set<String> newCellNames) {
		this.newCellNames = newCellNames;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkTargetWindow() {
		return linkTargetWindow;
	}

	public void setLinkTargetWindow(String linkTargetWindow) {
		this.linkTargetWindow = linkTargetWindow;
	}

	public List<LinkParameter> getLinkParameters() {
		return linkParameters;
	}

	public void setLinkParameters(List<LinkParameter> linkParameters) {
		this.linkParameters = linkParameters;
	}

	public String buildLinkParameters(Context context) {
		StringBuilder sb = new StringBuilder();
		if (linkParameters != null) {
			for (int i = 0; i < linkParameters.size(); i++) {
				LinkParameter param = linkParameters.get(i);
				String name = param.getName();
				if (linkParameterMap == null) {
					linkParameterMap = new HashMap<String, String>();
				}
				String value = linkParameterMap.get(name);
				if (value == null) {
					Expression expr = param.getValueExpression();
					value = buildExpression(context, name, expr);
				}
				try {
					value = URLEncoder.encode(value, "utf-8");
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					throw new ReportComputeException(e);
				}
				if (i > 0) {
					sb.append("&");
				}
				sb.append(name + "=" + value);
			}
		}
		return sb.toString();
	}

	public boolean isFillBlankRows() {
		return fillBlankRows;
	}

	public void setFillBlankRows(boolean fillBlankRows) {
		this.fillBlankRows = fillBlankRows;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public Expression getLinkUrlExpression() {
		return linkUrlExpression;
	}

	public void setLinkUrlExpression(Expression linkUrlExpression) {
		this.linkUrlExpression = linkUrlExpression;
	}
	

	private String buildExpression(Context context, String name, Expression expr) {
		ExpressionData<?> exprData = expr.execute(this, this, context);
		if (exprData instanceof ObjectListExpressionData) {
			ObjectListExpressionData listData = (ObjectListExpressionData) exprData;
			List<?> list = listData.getData();
			StringBuilder dataSB = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				if (obj == null) {
					obj = "null";
				}
				if (i > 0) {
					dataSB.append(",");
				}
				dataSB.append(obj);
			}
			linkParameterMap.put(name, dataSB.toString());
			return dataSB.toString();
		} else if (exprData instanceof ObjectExpressionData) {
			ObjectExpressionData data = (ObjectExpressionData) exprData;
			Object obj = data.getData();
			if (obj == null) {
				obj = "null";
			} else if (obj instanceof String) {
				obj = (String) obj;
			}
			linkParameterMap.put(name, obj.toString());
			return obj.toString();
		} else if (exprData instanceof BindDataListExpressionData) {
			BindDataListExpressionData bindDataListData = (BindDataListExpressionData) exprData;
			List<BindData> list = bindDataListData.getData();
			if (list.size() == 1) {
				Object data = list.get(0).getValue();
				if (data != null) {
					return data.toString();
				} else {
					return "";
				}
			} else if (list.size() > 1) {
				StringBuilder sb = new StringBuilder();
				for (BindData bindData : list) {
					if (sb.length() > 0) {
						sb.append(",");
					}
					Object data = bindData.getValue();
					if (data != null) {
						sb.append(data.toString());
					}
				}
				return sb.toString();
			}
		}
		return "";
	}
}
