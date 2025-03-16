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
package org.vivi.framework.ureport.simple.ureport.build.cell.down;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.build.Range;
import org.vivi.framework.ureport.simple.ureport.build.cell.DuplicateType;
import org.vivi.framework.ureport.simple.ureport.build.cell.ExpandBuilder;
import org.vivi.framework.ureport.simple.ureport.definition.BlankCellInfo;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionPropertyItem;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Row;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class DownExpandBuilder extends ExpandBuilder {
	@Override
	public Cell buildCell(List<BindData> dataList, Cell cell, Context context) {
		Range duplicateRange = cell.getDuplicateRange();
		int mainCellRowNumber = cell.getRow().getRowNumber();

		Range rowRange = buildRowRange(mainCellRowNumber, duplicateRange);

		DownDuplocatorWrapper downDuplocatorWrapper = buildCellDownDuplicator(cell, context, rowRange);

		int rowSize = rowRange.getEnd() - rowRange.getStart() + 1;
		DownBlankCellApply downBlankCellApply = new DownBlankCellApply(rowSize, cell, context, downDuplocatorWrapper);
		CellDownDuplicateUnit unit = new CellDownDuplicateUnit(context, downDuplocatorWrapper, cell, mainCellRowNumber,
				rowSize);
		Cell lastCell = cell;
		int dataSize = dataList.size();
		for (int i = 0; i < dataSize; i++) {
			BindData bindData = dataList.get(i);
			if (i == 0) {
				cell.setData(bindData.getValue());
				cell.setFormatData(bindData.getLabel());
				cell.setBindData(bindData.getDataList());
				List<ConditionPropertyItem> conditionPropertyItems = cell.getConditionPropertyItems();
				if (conditionPropertyItems != null && conditionPropertyItems.size() > 0) {
					context.getReport().getLazyComputeCells().add(cell);
				} else {
					cell.doFormat();
					cell.doDataWrapCompute(context);
				}
				continue;
			}
			boolean useBlankCell = downBlankCellApply.useBlankCell(i, bindData);
			if (useBlankCell) {
				continue;
			}
			Cell newCell = null;
			Cell newBlankCell = newBlankCell(cell, context, i);
			if (newBlankCell != null) {
				newCell = newBlankCell;
			} else {
				newCell = cell.newCell();
			}
			Cell leftParentCell = newCell.getLeftParentCell();
			if (leftParentCell != null) {
				leftParentCell.addRowChild(newCell);
			}
			Cell topParentCell = newCell.getTopParentCell();
			if (topParentCell != null) {
				topParentCell.addColumnChild(newCell);
			}
			newCell.setData(bindData.getValue());
			newCell.setFormatData(bindData.getLabel());
			newCell.setBindData(bindData.getDataList());
			newCell.setProcessed(true);
			if (newBlankCell == null) {
				unit.duplicate(newCell, i);
			}
			lastCell = newCell;
		}
		unit.complete();
		return lastCell;
	}

	private Cell newBlankCell(Cell cell, Context context, int index) {
		Cell leftParentCell = cell.getLeftParentCell();
		if (leftParentCell == null) {
			Row row = context.getRow(cell.getRow().getRowNumber() + index);
			if (row != null) {
				List<Cell> cells = row.getCells();
				for (Cell c : cells) {
					if (c.getName().equals(cell.getName())) {
						return c;
					}
				}
			}
		}
		return null;
	}

	private Range buildRowRange(int mainCellRowNumber, Range range) {
		int start = mainCellRowNumber + range.getStart();
		int end = mainCellRowNumber + range.getEnd();
		return new Range(start, end);
	}

	private DownDuplocatorWrapper buildCellDownDuplicator(Cell cell, Context context, Range range) {
		DownDuplocatorWrapper duplicatorWrapper = new DownDuplocatorWrapper(cell.getName());
		buildParentCellDuplicators(cell, cell, duplicatorWrapper);
		for (int i = range.getStart(); i <= range.getEnd(); i++) {
			Row row = context.getRow(i);
			if (row == null) {
				System.out.println(row);
			}
			List<Cell> rowCells = row.getCells();

			for (Cell rowCell : rowCells) {
				buildDuplicator(duplicatorWrapper, cell, rowCell, i);
			}

		}
		return duplicatorWrapper;
	}

	private void buildParentCellDuplicators(Cell cell, Cell mainCell, DownDuplocatorWrapper duplicatorWrapper) {
		Cell leftParentCell = cell.getLeftParentCell();
		if (leftParentCell == null) {
			return;
		}
		buildDuplicator(duplicatorWrapper, mainCell, leftParentCell, 0);
		buildParentCellDuplicators(leftParentCell, mainCell, duplicatorWrapper);
	}

	private void buildDuplicator(DownDuplocatorWrapper duplicatorWrapper, Cell mainCell, Cell currentCell,
			int rowNumber) {
		if (currentCell == mainCell) {
			return;
		}
		String name = currentCell.getName();
		Map<String, BlankCellInfo> newBlankCellNamesMap = mainCell.getNewBlankCellsMap();
		Set<String> increaseCellNames = mainCell.getIncreaseSpanCellNames();
		Set<String> newCellNames = mainCell.getNewCellNames();
		if (newBlankCellNamesMap.containsKey(name)) {
			if (!duplicatorWrapper.contains(currentCell)) {
				CellDownDuplicator cellDuplicator = new CellDownDuplicator(currentCell, DuplicateType.Blank,
						newBlankCellNamesMap.get(name), rowNumber);
				duplicatorWrapper.addCellDownDuplicator(cellDuplicator);
			}
		} else if (increaseCellNames.contains(name)) {
			if (!duplicatorWrapper.contains(currentCell)) {
				CellDownDuplicator cellDuplicator = new CellDownDuplicator(currentCell, DuplicateType.IncreseSpan,
						rowNumber);
				duplicatorWrapper.addCellDownDuplicator(cellDuplicator);
			}
		} else if (newCellNames.contains(name)) {
			CellDownDuplicator cellDuplicator = new CellDownDuplicator(currentCell, DuplicateType.Duplicate, rowNumber);
			duplicatorWrapper.addCellDownDuplicator(cellDuplicator);
		} else if (mainCell.getName().equals(name)) {
			CellDownDuplicator cellDuplicator = new CellDownDuplicator(currentCell, DuplicateType.Self, rowNumber);
			duplicatorWrapper.addCellDownDuplicator(cellDuplicator);
		}
	}
}
