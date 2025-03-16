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
package org.vivi.framework.ureport.simple.ureport.expression.model.expr.set;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.NoneExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Column;
import org.vivi.framework.ureport.simple.ureport.model.Row;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月1日
 */
public class CellExpression extends BaseExpression {
	private static final long serialVersionUID = 8376298136905903019L;
	protected String cellName;

	public CellExpression(String cellName) {
		this.cellName = cellName;
	}

	public boolean supportPaging() {
		return true;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Cell> targetCells = ToolUtils.fetchTargetCells(cell, context, cellName);
		if (targetCells == null || targetCells.size() == 0) {
			return new NoneExpressionData();
		}
		if (targetCells.size() == 1) {
			return new ObjectExpressionData(targetCells.get(0).getData());
		}
		if (targetCells.size() > 1) {
			List<Object> list = new ArrayList<Object>();
			for (Cell targetCell : targetCells) {
				list.add(targetCell.getData());
			}
			return new ObjectListExpressionData(list);
		}
		return new NoneExpressionData();
	}

	public ExpressionData<?> computePageCells(Cell cell, Cell currentCell, Context context) {
		int pageIndex = cell.getRow().getPageIndex();
		if (pageIndex == 0) {
			pageIndex = 1;
		}
		List<Object> list = new ArrayList<Object>();
		List<Row> pageRows = context.getCurrentPageRows(pageIndex);
		if(pageRows != null && pageRows.size() > 0) {
			Map<Row, Map<Column, Cell>> cellMap = context.getReport().getRowColCellMap();
			List<Column> columns = context.getReport().getColumns();
			for (Row row : pageRows) {
				Map<Column, Cell> map = cellMap.get(row);
				if (map == null) {
					continue;
				}
				for (Column col : columns) {
					Cell targetCell = map.get(col);
					if (targetCell == null || !targetCell.getName().equals(cellName)) {
						continue;
					}
					list.add(targetCell.getData());
				}
			}
		}
		return new ObjectListExpressionData(list);
	}

	protected List<Cell> fetchPageCells(Cell cell, Cell currentCell, Context context) {
		int pageIndex = cell.getRow().getPageIndex();
		if (pageIndex == 0) {
			pageIndex = 1;
		}
		List<Cell> list = new ArrayList<Cell>();
		List<Row> pageRows = context.getCurrentPageRows(pageIndex);
		if(pageRows != null && pageRows.size() > 0) {
			Map<Row, Map<Column, Cell>> cellMap = context.getReport().getRowColCellMap();
			List<Column> columns = context.getReport().getColumns();
			for (Row row : pageRows) {
				Map<Column, Cell> map = cellMap.get(row);
				if (map == null) {
					continue;
				}
				for (Column col : columns) {
					Cell targetCell = map.get(col);
					if (targetCell == null || !targetCell.getName().equals(cellName)) {
						continue;
					}
					list.add(targetCell);
				}
			}
		}
		return list;
	}
	
	public String getCellName() {
		return cellName;
	}

	@Override
	public void fetchCellName(List<String> list) {
		list.add(cellName);
	}
}
