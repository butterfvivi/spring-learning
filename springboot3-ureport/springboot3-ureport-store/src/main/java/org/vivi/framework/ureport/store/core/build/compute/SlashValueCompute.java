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
package org.vivi.framework.ureport.store.core.build.compute;

import org.vivi.framework.ureport.store.core.build.BindData;
import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.definition.value.SlashValue;
import org.vivi.framework.ureport.store.core.definition.value.ValueType;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.vivi.framework.ureport.store.core.model.Image;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jacky.gao
 * @since 2017年3月14日
 */
public class SlashValueCompute implements ValueCompute {
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		List<BindData> list=new ArrayList<BindData>();
		SlashValue v=(SlashValue)cell.getValue();
		Image img=new Image(v.getBase64Data(),"slash.png",0,0);
		BindData bindData=new BindData(img);
		list.add(bindData);
		/*String key=SlashBuilder.buildKey(context.getReport().getReportFullName(), cell.getName());
		Resource res=new Resource(key);
		BindData bindData=new BindData(res);
		list.add(bindData);*/
		return list;
	}


	@Override
	public ValueType type() {
		return ValueType.slash;
	}
}
