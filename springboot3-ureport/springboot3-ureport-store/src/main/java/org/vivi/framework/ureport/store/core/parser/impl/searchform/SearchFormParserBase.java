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
package org.vivi.framework.ureport.store.core.parser.impl.searchform;

import org.vivi.framework.ureport.store.core.definition.searchform.Component;
import org.vivi.framework.ureport.store.core.definition.searchform.FormPosition;
import org.vivi.framework.ureport.store.core.definition.searchform.SearchForm;
import org.vivi.framework.ureport.store.core.parser.Parser;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Jacky.gao
 * @since 2017年10月24日
 */
@Service
public class SearchFormParserBase extends FormParserBase implements Parser<SearchForm> {
	@Override
	public SearchForm parse(Element element) {
		SearchForm form=new SearchForm();
		form.setFormPosition(FormPosition.valueOf(element.attributeValue("form-position")));
		List<Component> components= formParse(element);
		form.setComponents(components);
		return form;
	}

	@Override
	public String getName() {
		return "search-form";
	}
}
