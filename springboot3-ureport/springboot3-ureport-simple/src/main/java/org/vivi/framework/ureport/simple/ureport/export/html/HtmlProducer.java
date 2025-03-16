package org.vivi.framework.ureport.simple.ureport.export.html;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.build.paging.Page;
import org.vivi.framework.ureport.simple.ureport.chart.ChartData;
import org.vivi.framework.ureport.simple.ureport.definition.Alignment;
import org.vivi.framework.ureport.simple.ureport.definition.Band;
import org.vivi.framework.ureport.simple.ureport.definition.Border;
import org.vivi.framework.ureport.simple.ureport.definition.CellStyle;
import org.vivi.framework.ureport.simple.ureport.definition.Paper;
import org.vivi.framework.ureport.simple.ureport.definition.ReportDefinition;
import org.vivi.framework.ureport.simple.ureport.export.PageBuilder;
import org.vivi.framework.ureport.simple.ureport.export.SinglePageData;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Column;
import org.vivi.framework.ureport.simple.ureport.model.Image;
import org.vivi.framework.ureport.simple.ureport.model.Report;
import org.vivi.framework.ureport.simple.ureport.model.Row;

/**
 * @author Jacky.gao
 * @since 2016年12月30日
 */
public class HtmlProducer {

	public HtmlReport produce(ReportDefinition reportDefinition, Report report) {
		HtmlReport htmlReport = new HtmlReport();
		Paper paper = reportDefinition.getPaper();
		Context context = report.getContext();
		Map<String, Object> parameters = context.getParameters();
		String html = null;
		if (paper.isPageEnabled()) { // 分页
			int pageIndex = Integer.parseInt(String.valueOf(parameters.get("pageIndex")));
			SinglePageData pageData = PageBuilder.buildSinglePageData(pageIndex, report);
			List<Page> pages = pageData.getPages();
			if (paper.isColumnEnabled()) { // 分栏
				List<Row> rows = report.getTitleRows();
				boolean hasTitleRows = false;
				if (rows != null && rows.size() > 0) {
					hasTitleRows = true;
				}
				int columnMargin = pageData.getColumnMargin();
				html = producePagesHtml(context, pages, columnMargin, hasTitleRows);
				htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
			} else {
				Page page = pages.get(0);
				html = producePageHtml(context, page, false, false);
			}
			htmlReport.setTotalPage(pageData.getTotalPages());
			htmlReport.setPageIndex(pageIndex);
		} else {
			List<Row> rows = report.getRows();
			List<Column> columns = report.getColumns();
			Map<Row, Map<Column, Cell>> cellMap = report.getRowColCellMap();
			StringBuilder sb = buildTable(report.getContext(), rows, columns, cellMap, false, false, false);
			html = sb.toString();
		}
		htmlReport.setChartDatas(context.getChartDataMap().values());
		htmlReport.setContent(html);
		htmlReport.setStyle(reportDefinition.getStyle());
		SearchFormData searchFormData = reportDefinition.buildSearchFormData(context.getDatasetMap(), parameters);
		htmlReport.setSearchFormData(searchFormData);
		htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
		htmlReport.setBgImage(report.getPaper().getBgImage());
		htmlReport.setFreeze(reportDefinition.getFreeze());
		htmlReport.setFloatImages(reportDefinition.getFloatImages());

		return htmlReport;
	}

	/**
	 * 分页
	 * 
	 * @param context
	 * @param page
	 * @return
	 */
	private String producePageHtml(Context context, Page page, boolean hasTitleRows, boolean isColumn) {
		List<Row> rows = page.getRows();
		List<Column> columns = page.getColumns();
		Map<Row, Map<Column, Cell>> cellMap = context.getReport().getRowColCellMap();
		StringBuilder sb = buildTable(context, rows, columns, cellMap, true, hasTitleRows, isColumn);
		return sb.toString();
	}

	/**
	 * 分栏
	 * 
	 * @param context
	 * @param pages
	 * @param columnMargin
	 * @return
	 */
	private String producePagesHtml(Context context, List<Page> pages, int columnMargin, boolean hasTitleRows) {
		int pageSize = pages.size();
		int singleTableWidth = buildTableWidth(pages.get(0).getColumns());
		int tableWidth = singleTableWidth * pageSize + columnMargin * (pageSize - 1);
		StringBuilder sb = new StringBuilder();

		sb.append("<table border='0' style='margin:auto;border-collapse:collapse;width:" + tableWidth + "pt'>");
		if (hasTitleRows) {
			// 标题行
			sb.append("<tr>");
			sb.append("<td colspan=" + (2 * pageSize - 1) + ">");
			sb.append(producePageHtml(context, pages.get(0), true, true));
			sb.append("</td>");
			sb.append("</tr>");
		}
		sb.append("<tr>");
		for (int i = 0; i < pageSize; i++) {
			if (i > 0) {
				sb.append("<td style='width:" + columnMargin + "pt'></td>");
			}
			Page page = pages.get(i);
			String table = producePageHtml(context, page, false, true);
			sb.append("<td style='width:" + singleTableWidth + "pt;vertical-align:top'>");
			sb.append(table);
			sb.append("</td>");
		}
		sb.append("</tr>");
		sb.append("</table>");
		return sb.toString();
	}

	private StringBuilder buildTable(Context context, List<Row> rows, List<Column> columns,
			Map<Row, Map<Column, Cell>> cellMap, boolean forPage, boolean hasTitleRows, boolean isColumn) {
		StringBuilder sb = new StringBuilder();
		int tableWidth = buildTableWidth(columns);
		if (hasTitleRows) {
			sb.append("<table border='0' style='word-wrap:break-word;word-break:break-all;white-space:normal;margin:auto;border-collapse:collapse;width:100%'>");
		} else {
			sb.append("<table border='0' style='word-wrap:break-word;word-break:break-all;white-space:normal;margin:auto;border-collapse:collapse;width:" + tableWidth + "pt'>");
		}
		// 最后一列为人工补的扩展列,需要去掉
		int colSize = columns.size() - 1;
		int rowSize = rows.size();
		for (int i = 0; i < rowSize; i++) {
			Row row = rows.get(i);
			if (!forPage && row.isForPaging()) {
				continue;
			}
			if(isColumn) {
				if (hasTitleRows && !Band.title.equals(row.getBand())) {
					break;
				} else if (!hasTitleRows && Band.title.equals(row.getBand())) {
					continue;
				}
			}
			int height = row.getRealHeight();
			if (height < 1) {
				continue;
			}
			sb.append("<tr style=\"height:" + height + "pt\">");
			for (int j = 0; j < colSize; j++) {
				Column col = columns.get(j);
				Cell cell = null;
				if (cellMap.containsKey(row)) {
					Map<Column, Cell> colMap = cellMap.get(row);
					if (colMap.containsKey(col)) {
						cell = colMap.get(col);
					}
				}
				if (cell == null || (!forPage && cell.isForPaging())) {
					continue;
				}

				int rowSpan = cell.getRowSpan();
				int pageRowSpan = cell.getPageRowSpan();
				int colSpan = cell.getColSpan();
				if (forPage) {
					rowSpan = pageRowSpan;
				}
				if (rowSpan > 0) {
					if (colSpan > 0) {
						sb.append("<td rowspan=\"" + rowSpan + "\" colspan=\"" + colSpan + "\"");
					} else {
						sb.append("<td rowspan=\"" + rowSpan + "\"");
					}
				} else {
					if (colSpan > 0) {
						sb.append("<td colspan=\"" + colSpan + "\"");
					} else {
						sb.append("<td");
					}
				}
				sb.append(" class='_" + cell.getName() + "' ");
				String style = buildCustomStyle(cell);
				sb.append(" " + style + "");
				sb.append(">");
				sb.append(buildCellValue(context, cell));
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb;
	}

	private String buildCustomStyle(Cell cell) {
		CellStyle style = cell.getCustomCellStyle();
		CellStyle rowStyle = cell.getRow().getCustomCellStyle();
		CellStyle colStyle = cell.getColumn().getCustomCellStyle();
		if (style == null && rowStyle == null && colStyle == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String forecolor = null;
		if (style != null) {
			forecolor = style.getForecolor();
		}
		if (rowStyle != null) {
			forecolor = rowStyle.getForecolor();
		}
		if (colStyle != null) {
			forecolor = colStyle.getForecolor();
		}
		if (StringUtils.isNotBlank(forecolor)) {
			sb.append("color:rgb(" + forecolor + ");");
		}
		String bgcolor = null;
		if (style != null) {
			bgcolor = style.getBgcolor();
		}
		if (rowStyle != null) {
			bgcolor = rowStyle.getBgcolor();
		}
		if (colStyle != null) {
			bgcolor = colStyle.getBgcolor();
		}
		if (StringUtils.isNotBlank(bgcolor)) {
			sb.append("background-color:rgb(" + bgcolor + ");");
		}
		String fontFamily = null;
		if (style != null) {
			fontFamily = style.getFontFamily();
		}
		if (rowStyle != null) {
			fontFamily = rowStyle.getFontFamily();
		}
		if (colStyle != null) {
			fontFamily = colStyle.getFontFamily();
		}
		if (StringUtils.isNotBlank(fontFamily)) {
			sb.append("font-family:" + fontFamily + ";");
		}
		int fontSize = 0;
		if (style != null) {
			fontSize = style.getFontSize();
		}
		if (rowStyle != null) {
			fontSize = rowStyle.getFontSize();
		}
		if (colStyle != null) {
			fontSize = colStyle.getFontSize();
		}
		if (fontSize > 0) {
			sb.append("font-size:" + fontSize + "pt;");
		}
		Boolean bold = null;
		if (style != null) {
			bold = style.getBold();
		}
		if (rowStyle != null) {
			bold = rowStyle.getBold();
		}
		if (colStyle != null) {
			bold = colStyle.getBold();
		}
		if (bold != null) {
			if (bold) {
				sb.append("font-weight:bold;");
			} else {
				sb.append("font-weight:normal;");
			}
		}
		Boolean italic = null;
		if (style != null) {
			italic = style.getItalic();
		}
		if (rowStyle != null) {
			italic = rowStyle.getItalic();
		}
		if (colStyle != null) {
			italic = colStyle.getItalic();
		}
		if (italic != null) {
			if (italic) {
				sb.append("font-style:italic;");
			} else {
				sb.append("font-style:normal;");

			}
		}
		Boolean underline = null;
		if (style != null) {
			underline = style.getUnderline();
		}
		if (rowStyle != null) {
			underline = rowStyle.getUnderline();
		}
		if (colStyle != null) {
			underline = colStyle.getUnderline();
		}
		if (underline != null) {
			if (underline) {
				sb.append("text-decoration:underline;");
			} else {
				sb.append("text-decoration:none;");
			}
		}
		Alignment align = null;
		if (style != null) {
			align = style.getAlign();
		}
		if (rowStyle != null) {
			align = rowStyle.getAlign();
		}
		if (colStyle != null) {
			align = colStyle.getAlign();
		}
		if (align != null) {
			sb.append("text-align:" + align.name() + ";");
		}
		Alignment valign = null;
		if (style != null) {
			valign = style.getValign();
		}
		if (rowStyle != null) {
			valign = rowStyle.getValign();
		}
		if (colStyle != null) {
			valign = colStyle.getValign();
		}
		if (valign != null) {
			sb.append("vertical-align:" + valign.name() + ";");
		}
		Border border = null;
		if (style != null) {
			border = style.getLeftBorder();
		}
		if (border != null) {
			sb.append("border-left:" + border.getStyle().name() + " " + border.getWidth() + "px rgb("
					+ border.getColor() + ");");
		}
		Border rightBorder = null;
		if (style != null) {
			rightBorder = style.getRightBorder();
		}
		if (rightBorder != null) {
			sb.append("border-right:" + rightBorder.getStyle().name() + " " + rightBorder.getWidth() + "px rgb("
					+ rightBorder.getColor() + ");");
		}
		Border topBorder = null;
		if (style != null) {
			topBorder = style.getTopBorder();
		}
		if (topBorder != null) {
			sb.append("border-top:" + topBorder.getStyle().name() + " " + topBorder.getWidth() + "px rgb("
					+ topBorder.getColor() + ");");
		}
		Border bottomBorder = null;
		if (style != null) {
			bottomBorder = style.getBottomBorder();
		}
		if (bottomBorder != null) {
			sb.append("border-bottom:" + bottomBorder.getStyle().name() + " " + bottomBorder.getWidth() + "px rgb("
					+ bottomBorder.getColor() + ");");
		}
		if (sb.length() > 0) {
			int colWidth = cell.getColumn().getWidth();
			sb.append("width:" + colWidth + "pt");
			sb.insert(0, "style=\"");
			sb.append("\"");
		}
		return sb.toString();
	}

	private int buildTableWidth(List<Column> columns) {
		int width = 0;
		// 最后一列为人工补的扩展列,不需要加入宽度
		for (int i = 0; i < columns.size() - 1; i++) {
			Column col = columns.get(i);
			width += col.getWidth();
		}
		return width;
	}

	private String buildCellValue(Context context, Cell cell) {
		StringBuilder sb = new StringBuilder();
		boolean hasLink = false;
		String linkURL = cell.getLinkUrl();
		if (StringUtils.isNotBlank(linkURL)) {
			Expression urlExpression = cell.getLinkUrlExpression();
			if (urlExpression != null) {
				ExpressionData<?> exprData = urlExpression.execute(cell, cell, context);
				if (exprData instanceof BindDataListExpressionData) {
					BindDataListExpressionData listExprData = (BindDataListExpressionData) exprData;
					List<BindData> bindDataList = listExprData.getData();
					if (bindDataList != null && bindDataList.size() > 0) {
						Object data = bindDataList.get(0).getValue();
						if (data != null) {
							linkURL = data.toString();
						}
					}
				} else if (exprData instanceof ObjectExpressionData) {
					ObjectExpressionData objExprData = (ObjectExpressionData) exprData;
					Object data = objExprData.getData();
					if (data != null) {
						linkURL = data.toString();
					}
				} else if (exprData instanceof ObjectListExpressionData) {
					ObjectListExpressionData objListExprData = (ObjectListExpressionData) exprData;
					List<?> list = objListExprData.getData();
					if (list != null && list.size() > 0) {
						Object data = list.get(0);
						if (data != null) {
							linkURL = data.toString();
						}
					}
				}
			}
			hasLink = true;
			String urlParameter = cell.buildLinkParameters(context);
			if (StringUtils.isNotBlank(urlParameter)) {
				if (linkURL.indexOf("?") == -1) {
					linkURL += "?" + urlParameter;
				} else {
					linkURL += "&" + urlParameter;
				}
			}
			String target = cell.getLinkTargetWindow();
			if (StringUtils.isBlank(target)) {
				target = "_self";
			}
			if ("_iframe".equals(target)) {
				sb.append("<a href=\"javascript: void(0);\" onclick=\"iframeWindow(this)\" url=\"" + linkURL + "\">");
			} else {
				sb.append("<a href=\"" + linkURL + "\" target=\"" + target + "\">");
			}
		}
		Object obj = (cell.getFormatData() == null) ? "" : cell.getFormatData();
		if (obj instanceof Image) {
			Image img = (Image) obj;
			String path = img.getPath();
			String imageType = "image/png";
			if (StringUtils.isNotBlank(path)) {
				path = path.toLowerCase();
				if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
					imageType = "image/jpeg";
				} else if (path.endsWith(".gif")) {
					imageType = "image/gif";
				}
			}
			sb.append("<img src=\"data:" + imageType + ";base64," + img.getBase64Data() + "\"");
			sb.append(">");
		} else if (obj instanceof ChartData) {
			ChartData chartData = (ChartData) obj;
			String canvasId = chartData.getId();
			sb.append("<div id=\"" + canvasId + "\" class=\"charts-item-content\"></div>");
		} else {
			String text = obj.toString();
			text = StringEscapeUtils.escapeHtml3(text);
			text = text.replaceAll("\r\n", "<br>");
			text = text.replaceAll("\n", "<br>");
			text = text.replaceAll(" ", "&nbsp;");
			if (text.equals("")) {
				text = "&nbsp;";
			}
			sb.append(text);
		}
		if (hasLink) {
			sb.append("</a>");
		}
		return sb.toString();
	}
}