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
package org.vivi.framework.ureport.simple.ureport.expression.model.expr;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.NoneExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年7月11日
 */
public class CurrentCellDataExpression extends BaseExpression {
	
	private static final long serialVersionUID = 7517926036810650110L;
	
	private String property;
	
	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell,Context context) {
		List<Object> bindDataList=cell.getBindData();
		if(bindDataList==null || bindDataList.size()==0){
			return new NoneExpressionData();
		}
		Object obj=bindDataList.get(0);
		Object data=ToolUtils.getProperty(obj, property);
		return new ObjectExpressionData(data);
	}
	public void setProperty(String property) {
		this.property = property;
	}
	@Override
	public void fetchCellName(List<String> list) {
		
	}
}
