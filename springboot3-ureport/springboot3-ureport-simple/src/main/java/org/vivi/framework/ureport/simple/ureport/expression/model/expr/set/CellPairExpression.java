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

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.ExpressionUtils;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月1日
 */
public class CellPairExpression extends BaseExpression {

	private static final long serialVersionUID = 775139518725235246L;

	private String startCellName;

	private String endCellName;

	public CellPairExpression(String startCellName, String endCellName) {
		this.startCellName = startCellName;
		this.endCellName = endCellName;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Cell> cellList = buildCells(cell, context);
		if (cellList.size() > 1) {
			List<Object> list = new ArrayList<Object>();
			for (Cell targetCell : cellList) {
				list.add(targetCell.getData());
			}
			return new ObjectListExpressionData(list);
		} else if (cellList.size() == 1) {
			return new ObjectExpressionData(cellList.get(0).getData());
		} else {
			return new ObjectExpressionData(null);
		}
	}

	private List<Cell> buildCells(Cell cell, Context context) {
		List<String> cellNameList = ExpressionUtils.getCellNameList();
		CellName startName = parseCellName(startCellName);
		int startPos = cellNameList.indexOf(startName.getName());
		int rowStart = startName.getNumber();

		CellName endName = parseCellName(endCellName);
		int endPos = cellNameList.indexOf(endName.getName());
		int rowEnd = endName.getNumber();

		if (startPos > endPos) {
			int tmp = startPos;
			startPos = endPos;
			endPos = tmp;
		}
		if (rowStart > rowEnd) {
			int tmp = rowStart;
			rowStart = rowEnd;
			rowEnd = tmp;
		}

		List<String> names = new ArrayList<String>();
		for (int i = startPos; i <= endPos; i++) {
			names.add(cellNameList.get(i));
		}

		List<Cell> cellList = new ArrayList<Cell>();
		for (String name : names) {
			for (int i = rowStart; i <= rowEnd; i++) {
				String cellName = name + i;
				List<Cell> cells = ToolUtils.fetchTargetCells(cell, context, cellName);
				cellList.addAll(cells);
			}
		}
		return cellList;
	}

	private CellName parseCellName(String name) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isDigit(c)) {
				sb1.append(c);
			} else {
				sb.append(c);
			}
		}
		String cellName = sb.toString();
		int number = Integer.valueOf(sb1.toString());
		return new CellName(cellName, number);
	}

	public List<String> getCellName() {
		List<String> cellNameList = ExpressionUtils.getCellNameList();
		CellName startName = parseCellName(startCellName);
		int startPos = cellNameList.indexOf(startName.getName());
		int rowStart = startName.getNumber();

		CellName endName = parseCellName(endCellName);
		int endPos = cellNameList.indexOf(endName.getName());
		int rowEnd = endName.getNumber();

		if (startPos > endPos) {
			int tmp = startPos;
			startPos = endPos;
			endPos = tmp;
		}
		if (rowStart > rowEnd) {
			int tmp = rowStart;
			rowStart = rowEnd;
			rowEnd = tmp;
		}
		List<String> names = new ArrayList<String>();
		for (int i = startPos; i <= endPos; i++) {
			names.add(cellNameList.get(i));
		}
		return names;
	}

	@Override
	public void fetchCellName(List<String> list) {
		List<String> cellNameList = ExpressionUtils.getCellNameList();
		CellName startName = parseCellName(startCellName);
		int startPos = cellNameList.indexOf(startName.getName());
		int rowStart = startName.getNumber();

		CellName endName = parseCellName(endCellName);
		int endPos = cellNameList.indexOf(endName.getName());
		int rowEnd = endName.getNumber();

		if (startPos > endPos) {
			int tmp = startPos;
			startPos = endPos;
			endPos = tmp;
		}
		if (rowStart > rowEnd) {
			int tmp = rowStart;
			rowStart = rowEnd;
			rowEnd = tmp;
		}
		for (int i = startPos; i <= endPos; i++) {
			list.add(cellNameList.get(i));
		}
	}
}

class CellName {
	private String name;
	private int number;

	public CellName(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}
}
