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

import org.vivi.framework.ureport.store.core.definition.searchform.LabelPosition;
import org.vivi.framework.ureport.store.core.definition.searchform.TextInputComponent;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

/**
 * @author Jacky.gao
 * @since 2017年10月24日
 */
@Component
public class TextInputParser implements FormParser<TextInputComponent> {
	@Override
	public TextInputComponent parse(Element element) {
		TextInputComponent component=new TextInputComponent();
		component.setBindParameter(element.attributeValue("bind-parameter"));
		component.setLabel(element.attributeValue("label"));
		component.setType(element.attributeValue("type"));
		component.setLabelPosition(LabelPosition.valueOf(element.attributeValue("label-position")));
		return component;
	}

	@Override
	public String getName() {
		return "input-text";
	}
}
