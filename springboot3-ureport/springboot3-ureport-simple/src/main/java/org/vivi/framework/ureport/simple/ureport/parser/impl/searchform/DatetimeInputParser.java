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
package org.vivi.framework.ureport.simple.ureport.parser.impl.searchform;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.searchform.DateInputComponent;

/**
 * @author Jacky.gao
 * @since 2017年10月24日
 */
public class DatetimeInputParser implements FormParser<DateInputComponent> {
	@Override
	public DateInputComponent parse(Element element) {

		DateInputComponent component = new DateInputComponent();

		component.setBindParameter(element.attributeValue("bind-parameter"));

		component.setLabel(element.attributeValue("label"));
		
		component.setType(element.attributeValue("type"));
		
		component.setUuid(element.attributeValue("uuid"));
		
		component.setFormat(element.attributeValue("format"));
		
		component.setDefaultValue(element.attributeValue("default-value"));
		
		return component;
	}

	@Override
	public boolean support(String name) {
		return name.equals("input-datetime");
	}
}
