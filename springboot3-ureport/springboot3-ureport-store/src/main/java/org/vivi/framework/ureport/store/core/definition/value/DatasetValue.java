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
package org.vivi.framework.ureport.store.core.definition.value;

import org.vivi.framework.ureport.store.core.expression.model.expr.dataset.DatasetExpression;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class DatasetValue extends DatasetExpression implements Value{
	private static final long serialVersionUID = 1892973888854385049L;

	@Override
	public ValueType getType() {
		return ValueType.dataset;
	}
	
	@Override
	public String getValue() {
		StringBuffer sb=new StringBuffer();
		sb.append(getDatasetName());
		sb.append(".");
		sb.append(getAggregate().name());
		sb.append("(");
		String prop=getProperty();
		if(prop!=null){
			if(prop.length()>13){
				prop=prop.substring(0,10)+"...";
			}
			sb.append(prop);
		}
		sb.append(")");
		return sb.toString();
	}
}
