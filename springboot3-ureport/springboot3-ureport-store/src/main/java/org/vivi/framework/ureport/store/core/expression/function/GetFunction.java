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
package org.vivi.framework.ureport.store.core.expression.function;


import org.vivi.framework.ureport.store.core.Utils;
import org.vivi.framework.ureport.store.core.build.BindData;
import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年9月5日
 */
@Component
public class GetFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		int index=1;
		String propertyName=null;
		List<Object> list= new ArrayList<Object>();
		if(dataList.size()==1){
			buildList(dataList.get(0),list);
		}else if(dataList.size()==2){
			buildList(dataList.get(0),list);
			index = buildIndex(dataList.get(1));
		}else if(dataList.size()==3){
			buildList(dataList.get(0),list);
			index = buildIndex(dataList.get(1));
			ExpressionData<?> d=dataList.get(2);
			if(d instanceof ObjectExpressionData){
				ObjectExpressionData exprData=(ObjectExpressionData)d;
				Object obj=exprData.getData();
				if(obj!=null){
					propertyName=obj.toString();
				}
			}
		}
		Object obj=null;
		if(list.size()>0){
			if(index<=list.size()){
				obj = list.get(index-1);
			}else{
				obj = list.get(list.size()-1);
			}
		}
		if(StringUtils.isNotBlank(propertyName)){
			obj= Utils.getProperty(obj, propertyName);
		}
		return obj;
	}

	private int buildIndex(ExpressionData<?> d) {
		int index=1;
		if(d instanceof ObjectExpressionData){
			ObjectExpressionData exprData=(ObjectExpressionData)d;
			Object obj=exprData.getData();
			if(obj!=null){
				index=Utils.toBigDecimal(obj).intValue();
			}
		}
		return index;
	}

	private void buildList(ExpressionData<?> d, List<Object> list) {
		if(d instanceof ObjectExpressionData){
			ObjectExpressionData exprData=(ObjectExpressionData)d;
			list.add(exprData.getData());
		}else if(d instanceof ObjectListExpressionData){
			ObjectListExpressionData listData=(ObjectListExpressionData)d;
			list.addAll(listData.getData());
		}else if(d instanceof BindDataListExpressionData){
			BindDataListExpressionData listData=(BindDataListExpressionData)d;
			for(BindData bindData:listData.getData()){
				list.addAll(bindData.getDataList());
			}
		}
	}

	@Override
	public String name() {
		return "get";
	}
}
