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

import org.vivi.framework.ureport.simple.ureport.definition.Band;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class Row extends Line {

	private int height;

	private int realHeight = -1;

	private String rowKey;

	private Band band;

	private int pageIndex;

	private boolean forPaging;

	private boolean pageBreak;

	private boolean hide;

	private int rowNumber;
	
	public Row newRow() {
		Row row = new Row();
		row.setHeight(height);
		row.setRealHeight(realHeight);
		row.setBand(band);
		row.setRowKey(rowKey);
		row.setCustomCellStyle(getCustomCellStyle());
		return row;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getRowNumber() {
		return this.rowNumber;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Band getBand() {
		return band;
	}

	public void setBand(Band band) {
		this.band = band;
	}

	public boolean isForPaging() {
		return forPaging;
	}

	public void setForPaging(boolean forPaging) {
		this.forPaging = forPaging;
	}

	public boolean isPageBreak() {
		return pageBreak;
	}

	public void setPageBreak(boolean pageBreak) {
		this.pageBreak = pageBreak;
	}

	public int getRealHeight() {
		if (realHeight == -1) {
			return height;
		}
		return realHeight;
	}

	public void setRealHeight(int realHeight) {
		this.realHeight = realHeight;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
}
