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
package org.vivi.framework.ureport.simple.ureport.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.exception.ConvertException;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BothExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.CellExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.CurrentValueExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.PropertyExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Report;

/**
 * @author Jacky.gao
 * @since 2016年11月12日
 */
public class ToolUtils {

	private static boolean debug = true;

	public static boolean isDebug() {
		return ToolUtils.debug;
	}

	public static String format(double value, Integer decimal) {
		if (decimal != null) {
			return String.format("%." + decimal.intValue() + "f", value);
		}
		return String.valueOf(value);
	}

	public static void logToConsole(String msg) {
		if (ToolUtils.debug) {
			System.out.println(msg);
		}
	}

	public static List<Cell> fetchTargetCells(Cell cell, Context context, String cellName) {
		while (!context.isCellPocessed(cellName)) {
			context.getReportBuilder().buildCell(context, null);
		}
		List<Cell> leftCells = fetchCellsByLeftParent(context, cell, cellName);
		List<Cell> topCells = fetchCellsByTopParent(context, cell, cellName);
		if (leftCells != null && topCells != null) {
			List<Cell> bigger = null;
			List<Cell> smaller = null;
			if (leftCells.size() > topCells.size()) {
				bigger = leftCells;
				smaller = topCells;
			} else {
				bigger = topCells;
				smaller = leftCells;
			}
			List<Cell> list = new ArrayList<Cell>();
			Set<Cell> set = new HashSet<Cell>();
			for (Cell c : smaller) {
				set.add(c);
			}
			for (Cell c : bigger) {
				if (set.contains(c)) {
					list.add(c);
				}
			}
			set = null;
			return list;
		} else if (leftCells != null) {
			return leftCells;
		} else if (topCells != null) {
			return topCells;
		} else {
			Report report = context.getReport();
			return report.getCellsMap().get(cellName);
		}
	}

	public static Object getProperty(Object obj, String property) {
		if (obj == null)
			return null;
		try {
			if (obj instanceof Map && property.indexOf(".") == -1) {
				Map<?, ?> map = (Map<?, ?>) obj;
				return map.get(property);
			}
			return PropertyUtils.getProperty(obj, property);
		} catch (Exception ex) {
			throw new ReportComputeException(ex);
		}
	}

	public static BigDecimal toBigDecimal(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		} 
		String val = String.valueOf(obj).trim();
		if (StringUtils.isBlank(val)) {
			return new BigDecimal(0);
		}
		if(!NumberUtils.isCreatable(val)) {
			throw new ConvertException("Can not convert " + obj + " to BigDecimal.");
		}
		return new BigDecimal(val);
	}

	public static Integer toInteger(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof String) {
			if (obj.toString().trim().equals("")) {
				return 0;
			}
			try {
				String str = obj.toString().trim();
				return new Integer(str);
			} catch (Exception ex) {
				throw new ConvertException("Can not convert " + obj + " to Integer.");
			}
		}
		throw new ConvertException("Can not convert " + obj + " to Integer.");
	}

	public void setDebug(boolean debug) {
		ToolUtils.debug = debug;
	}

	public static List<Cell> fetchCellsByLeftParent(Context context, Cell cell, String cellName) {
		Cell leftParentCell = cell.getLeftParentCell();
		if (leftParentCell == null) {
			return null;
		}
		if (leftParentCell.getName().equals(cellName)) {
			List<Cell> list = new ArrayList<Cell>();
			list.add(leftParentCell);
			return list;
		}
		Set<String> rowChildCellNames = leftParentCell.getRowChildCellNames();
		if (rowChildCellNames != null && rowChildCellNames.contains(cellName)) {
			return leftParentCell.getRowChildrenCells(cellName);
		}
		return fetchCellsByLeftParent(context, leftParentCell, cellName);
	}

	public static List<Cell> fetchCellsByTopParent(Context context, Cell cell, String cellName) {
		Cell topParentCell = cell.getTopParentCell();
		if (topParentCell == null) {
			return null;
		}
		if (topParentCell.getName().equals(cellName)) {
			List<Cell> list = new ArrayList<Cell>();
			list.add(topParentCell);
			return list;
		}
		Set<String> columnChildCellNames = topParentCell.getColumnChildCellNames();
		if (columnChildCellNames != null && columnChildCellNames.contains(cellName)) {
			return topParentCell.getColumnChildrenCells(cellName);
		}
		return fetchCellsByTopParent(context, topParentCell, cellName);
	}

	public static void fetchCellName(List<Condition> conditions, List<String> list) {
		if (conditions == null || conditions.size() == 0) {
			return;
		}
		for (Condition condition : conditions) {
			fetchCellName(condition, list);
		}
	}
	
	public static void fetchCellName(Condition condition, List<String> list) {
		if (condition instanceof BothExpressionCondition) {
			BothExpressionCondition bothExpressionCondition = (BothExpressionCondition) condition;
			bothExpressionCondition.fetchCellName(list);
		} else if (condition instanceof CellExpressionCondition) {
			CellExpressionCondition cellExpressionCondition = (CellExpressionCondition) condition;
			cellExpressionCondition.fetchCellName(list);
		} else if (condition instanceof CurrentValueExpressionCondition) {
			CurrentValueExpressionCondition currentValueExpressionCondition = (CurrentValueExpressionCondition) condition;
			currentValueExpressionCondition.fetchCellName(list);
		} else if (condition instanceof PropertyExpressionCondition) {
			PropertyExpressionCondition propertyExpressionCondition = (PropertyExpressionCondition) condition;
			propertyExpressionCondition.fetchCellName(list);
		}
	}
	
	public static Object format(Object value, String format) {
		if(value == null || StringUtils.isBlank(format)) {
			return value;
		}
		if (value instanceof Date) {
			Date d = (Date) value;
			LocalDateTime localDateTime = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			DateTimeFormatter dt = DateTimeFormatter.ofPattern(format);
			return dt.format(localDateTime);
		}
		if (value instanceof LocalDateTime) {
			LocalDateTime d = (LocalDateTime) value;
			DateTimeFormatter dt = DateTimeFormatter.ofPattern(format);
			return dt.format(d);
		}
		if (value instanceof LocalDate) {
			LocalDate d = (LocalDate) value;
			DateTimeFormatter dt = DateTimeFormatter.ofPattern(format);
			return dt.format(d);
		}
		String val = String.valueOf(value).trim();
		if(NumberUtils.isCreatable(val)) {
			BigDecimal bigDecimal = toBigDecimal(value);
			if(bigDecimal != null) {
				DecimalFormat df = new DecimalFormat(format);
				return df.format(bigDecimal.doubleValue());
			}
		}
		return value;
	}
}
