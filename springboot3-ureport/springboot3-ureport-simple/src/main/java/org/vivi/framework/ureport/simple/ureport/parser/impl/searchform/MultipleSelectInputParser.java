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
import java.util.List;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.common.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.MultipleSelectInputComponent;
import org.vivi.framework.ureport.simple.ureport.definition.searchform.Option;

/**
 * @author Jacky.gao
 * @since 2017年10月30日
 */
public class MultipleSelectInputParser implements FormParser<MultipleSelectInputComponent> {
	
	@Override
	public MultipleSelectInputComponent parse(Element element) {
		MultipleSelectInputComponent select = new MultipleSelectInputComponent();
		select.setBindParameter(element.attributeValue("bind-parameter"));
		select.setLabel(element.attributeValue("label"));
		select.setType(element.attributeValue("type"));
		select.setUuid(element.attributeValue("uuid"));
		String constant = element.attributeValue("constant");
		if (StringUtils.isNotBlank(constant) && "1".equals(constant)) {
			select.setConstant(constant);
			select.setDataset(element.attributeValue("dataset"));
			select.setLabelField(element.attributeValue("label-field"));
			select.setValueField(element.attributeValue("value-field"));
		}
		List<Option> options = new ArrayList<Option>();
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			if (!ele.getName().equals("option")) {
				continue;
			}
			Option option = new Option();
			options.add(option);
			option.setLabel(ele.attributeValue("label"));
			option.setValue(ele.attributeValue("value"));
		}
		select.setOptions(options);
		return select;
	}

	@Override
	public boolean support(String name) {
		return name.equals("input-multiple-select");
	}
}
