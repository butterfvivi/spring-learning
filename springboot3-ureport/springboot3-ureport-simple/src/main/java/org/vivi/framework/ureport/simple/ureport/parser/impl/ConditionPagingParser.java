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
package org.vivi.framework.ureport.simple.ureport.parser.impl;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.ConditionPaging;
import org.vivi.framework.ureport.simple.ureport.definition.PagingPosition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2017年6月21日
 */
public class ConditionPagingParser implements Parser<ConditionPaging> {
	
	public final static Parser<ConditionPaging> instance = new ConditionPagingParser();
	
	@Override
	public ConditionPaging parse(Element element) {
		ConditionPaging paging=new ConditionPaging();
		String position=element.attributeValue("position");
		paging.setPosition(PagingPosition.valueOf(position));
		String line=element.attributeValue("line");
		paging.setLine(Integer.valueOf(line));
		return paging;
	}

	@Override
	public boolean support(String name) {
		return name.equals("paging");
	}
}
