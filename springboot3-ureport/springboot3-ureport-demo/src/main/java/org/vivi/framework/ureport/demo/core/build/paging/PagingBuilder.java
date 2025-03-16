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
package org.vivi.framework.ureport.demo.core.build.paging;

import org.vivi.framework.ureport.demo.core.build.BindData;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.definition.PagingMode;
import org.vivi.framework.ureport.demo.core.definition.Paper;
import org.vivi.framework.ureport.demo.core.model.Cell;
import org.vivi.framework.ureport.demo.core.model.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PagingBuilder {
    private static Map<PagingMode, Pagination> paginationMap = new HashMap<PagingMode, Pagination>();

    static {
        paginationMap.put(PagingMode.fitpage, new FitPagePagination());
        paginationMap.put(PagingMode.fixrows, new FixRowsPagination());
    }

    public static List<Page> buildPages(Report report) {
        Paper paper = report.getPaper();
        Pagination pagination = paginationMap.get(paper.getPagingMode());
        List<Page> pages = pagination.doPaging(report);
        computeExistPageFunctionCells(report);
        return pages;
    }

    public static void computeExistPageFunctionCells(Report report) {
        Context context = report.getContext();
        List<Cell> existPageFunctionCells = context.getExistPageFunctionCells();
        for (Cell cell : existPageFunctionCells) {
            List<BindData> dataList = context.buildCellData(cell);
            if (dataList == null || dataList.size() == 0) {
                continue;
            }
            BindData bindData = dataList.get(0);
            cell.setData(bindData.getValue());
            cell.setBindData(bindData.getDataList());
            cell.doFormat();
            cell.doDataWrapCompute(context);
        }
    }
}
