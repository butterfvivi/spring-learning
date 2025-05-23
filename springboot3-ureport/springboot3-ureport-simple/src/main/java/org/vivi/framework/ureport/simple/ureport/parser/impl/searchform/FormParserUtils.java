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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.searchform.Component;

/**
 * @author Jacky.gao
 * @since 2017年10月24日
 */
public class FormParserUtils {

	@SuppressWarnings("rawtypes")
	private static Collection<FormParser> parsers = new ArrayList<FormParser>();
	
	static {
		
		parsers.add(new CheckboxParser());
		parsers.add(new DatetimeInputParser());
		parsers.add(new RadioInputParser());
		parsers.add(new SelectInputParser());
		parsers.add(new MultipleSelectInputParser());
		parsers.add(new TextInputParser());
	}

	public static List<Component> parse(Element element) {
		List<Component> list = new ArrayList<Component>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String name = ele.getName();
			FormParser<?> targetParser = null;
			for (FormParser<?> parser : parsers) {
				if (parser.support(name)) {
					targetParser = parser;
					break;
				}
			}
			if (targetParser == null) {
				continue;
			}
			list.add((Component) targetParser.parse(ele));
		}
		return list;
	}
}
