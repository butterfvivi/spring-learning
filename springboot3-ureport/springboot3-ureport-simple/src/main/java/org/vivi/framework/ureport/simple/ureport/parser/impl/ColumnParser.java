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

import org.vivi.framework.ureport.simple.common.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.definition.ColumnDefinition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2016年12月5日
 */
public class ColumnParser implements Parser<ColumnDefinition> {
	
	public final static Parser<ColumnDefinition> instance = new ColumnParser();
	
	@Override
	public ColumnDefinition parse(Element element) {
		ColumnDefinition col = new ColumnDefinition();
		col.setColumnNumber(Integer.valueOf(element.attributeValue("col-number")));
		String hide = element.attributeValue("hide");
		if (StringUtils.isNotBlank(hide)) {
			col.setHide(Boolean.valueOf(hide));
		}
		String width = element.attributeValue("width");
		if (StringUtils.isNotBlank(width)) {
			col.setWidth(Integer.valueOf(width));
		}
		return col;
	}

	@Override
	public boolean support(String name) {
		return name.equals("column");
	}
}
