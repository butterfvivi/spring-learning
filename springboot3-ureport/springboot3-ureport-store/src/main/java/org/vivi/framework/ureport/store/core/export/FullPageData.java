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
package org.vivi.framework.ureport.store.core.export;

import org.vivi.framework.ureport.store.core.build.paging.Page;

import java.util.List;


/**
 * @author Jacky.gao
 * @since 2017年3月23日
 */
public class FullPageData {
	private int totalPages;
	private int columnMargin;
	private List<List<Page>> pageList;
	
	public FullPageData(int totalPages, int columnMargin,List<List<Page>> pageList) {
		this.totalPages = totalPages;
		this.columnMargin = columnMargin;
		this.pageList = pageList;
	}
	public int getColumnMargin() {
		return columnMargin;
	}
	public List<List<Page>> getPageList() {
		return pageList;
	}
	public int getTotalPages() {
		return totalPages;
	}
}
