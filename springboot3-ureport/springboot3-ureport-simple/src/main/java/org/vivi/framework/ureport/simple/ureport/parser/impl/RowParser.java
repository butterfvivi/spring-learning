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
import org.vivi.framework.ureport.simple.ureport.definition.Band;
import org.vivi.framework.ureport.simple.ureport.definition.RowDefinition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2016年12月5日
 */
public class RowParser implements Parser<RowDefinition> {
	
	public final static Parser<RowDefinition> instance = new RowParser();

	@Override
	public RowDefinition parse(Element element) {
		RowDefinition row = new RowDefinition();
		row.setRowNumber(Integer.valueOf(element.attributeValue("row-number")));
		String height = element.attributeValue("height");
		if (StringUtils.isNotBlank(height)) {
			row.setHeight(Integer.valueOf(height));
		}
		String band = element.attributeValue("band");
		if (StringUtils.isNotBlank(band)) {
			row.setBand(Band.valueOf(band));
		}
		return row;
	}

	@Override
	public boolean support(String name) {
		return name.equals("row");
	}
}
