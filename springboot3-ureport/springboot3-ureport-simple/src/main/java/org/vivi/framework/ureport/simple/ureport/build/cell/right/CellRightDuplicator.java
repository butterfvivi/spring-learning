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
package org.vivi.framework.ureport.simple.ureport.build.cell.right;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.build.cell.DuplicateType;
import org.vivi.framework.ureport.simple.ureport.definition.BlankCellInfo;
import org.vivi.framework.ureport.simple.ureport.definition.value.SimpleValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.Value;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.model.Column;

/**
 * @author Jacky.gao
 * @since 2016年11月7日
 */
public class CellRightDuplicator {
	private Cell cell;
	private int cellColNumber;
	private DuplicateType duplicateType;
	private BlankCellInfo blankCellInfo;
	private boolean nonChild = false;

	public CellRightDuplicator(Cell cell, DuplicateType duplicateType, int cellColNumber) {
		this.cell = cell;
		this.duplicateType = duplicateType;
		this.cellColNumber = cellColNumber;
	}

	public CellRightDuplicator(Cell cell, DuplicateType duplicateType, BlankCellInfo blankCellInfo, int cellColNumber) {
		this.cell = cell;
		this.duplicateType = duplicateType;
		this.blankCellInfo = blankCellInfo;
		if (cellColNumber > 0) {
			this.cellColNumber = cellColNumber;
		} else {
			this.cellColNumber = cell.getColumn().getColumnNumber();
		}
	}

	public Cell duplicate(RightDuplicate rightDuplicate, Cell newMainCell) {
		switch (duplicateType) {
		case Blank:
			processBlankCell(rightDuplicate, newMainCell);
			break;
		case Self:
			processSelfBlankCell(rightDuplicate);
			break;
		case IncreseSpan:
			processIncreaseSpanCell(rightDuplicate);
			break;
		case Duplicate:
			throw new ReportComputeException("Invalid duplicator.");
		}
		return null;
	}

	private void processSelfBlankCell(RightDuplicate rightDuplicate) {
		Cell newBlankCell = cell.newCell();
		newBlankCell.setValue(new SimpleValue(""));
		Column newCol = rightDuplicate.newColumn(newBlankCell.getColumn(), cellColNumber);
		newCol.getCells().add(newBlankCell);
		newBlankCell.getRow().getCells().add(newBlankCell);
		newBlankCell.setColumn(newCol);
		Context context = rightDuplicate.getContext();
		context.addBlankCell(newBlankCell);
	}

	public Cell duplicateChildrenCell(RightDuplicate rightDuplicate, Cell topParent, Cell originalCell,
			boolean parentNonChild) {
		Cell newCell = cell.newCell();
		Column newCol = rightDuplicate.newColumn(newCell.getColumn(), cellColNumber);
		newCol.getCells().add(newCell);
		newCell.getRow().getCells().add(newCell);
		newCell.setColumn(newCol);
		if (newCell.getTopParentCell() == originalCell) {
			newCell.setTopParentCell(topParent);
			if (parentNonChild) {
				nonChild = true;
			}
		} else {
			nonChild = true;
		}
		Cell leftParentCell = newCell.getLeftParentCell();
		if (leftParentCell != null) {
			leftParentCell.addRowChild(newCell);
		}
		Cell topParentCell = newCell.getTopParentCell();
		if (topParentCell != null) {
			topParentCell.addColumnChild(newCell);
		}
		Context context = rightDuplicate.getContext();
		Value value = newCell.getValue();
		if (value instanceof SimpleValue) {
			newCell.setData(value.getValue());
			newCell.setProcessed(true);
			context.addReportCell(newCell);
		} else {
			if (nonChild) {
				newCell.setValue(new SimpleValue(""));
				context.addBlankCell(newCell);
			} else {
				context.addCell(newCell);
			}
		}
		return newCell;
	}

	private void processIncreaseSpanCell(RightDuplicate rightDuplicate) {
		int colSpan = cell.getColSpan();
		colSpan += rightDuplicate.getColSize();
		if (colSpan == 1) {
			colSpan++;
		}
		cell.setColSpan(colSpan);
	}

	private void processBlankCell(RightDuplicate rightDuplicate, Cell newMainCell) {
		Context context = rightDuplicate.getContext();
		Cell newBlankCell = cell.newColumnBlankCell(context, blankCellInfo, rightDuplicate.getMainCell());
		if (blankCellInfo.isParent() && newMainCell.getTopParentCell() == cell) {
			newMainCell.setTopParentCell(newBlankCell);
		}
		Column col = rightDuplicate.newColumn(newBlankCell.getColumn(), cellColNumber);
		col.getCells().add(newBlankCell);
		newBlankCell.getRow().getCells().add(newBlankCell);
		newBlankCell.setColumn(col);
		context.addReportCell(newBlankCell);
	}

	public DuplicateType getDuplicateType() {
		return duplicateType;
	}

	public Cell getCell() {
		return cell;
	}

	public boolean isNonChild() {
		return nonChild;
	}

	public void setNonChild(boolean nonChild) {
		this.nonChild = nonChild;
	}
}
