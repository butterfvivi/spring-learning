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

import java.util.List;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.searchform.Component;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.SearchForm;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2017年10月24日
 */
public class SearchFormParser implements Parser<SearchForm> {
	
	public final static Parser<SearchForm> instance = new SearchFormParser();

	@Override
	public SearchForm parse(Element element) {
		SearchForm form = new SearchForm();
		List<Component> components = FormParserUtils.parse(element);
		form.setComponents(components);
		return form;
	}

	@Override
	public boolean support(String name) {
		return name.equals("search-form");
	}
}
