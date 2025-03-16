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
package org.vivi.framework.ureport.simple.ureport.parser.impl.value;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.value.SimpleValue;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class SimpleValueParser implements Parser<SimpleValue> {
	
	public final static Parser<SimpleValue> instance = new SimpleValueParser();
	
	@Override
	public SimpleValue parse(Element element) {
		SimpleValue simpleValue = new SimpleValue(element.getText());
		return simpleValue;
	}

	@Override
	public boolean support(String name) {
		return name.equals("simple-value");
	}
}
