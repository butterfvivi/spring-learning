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
package org.vivi.framework.ureport.store.core.parser.impl.value;


import org.vivi.framework.ureport.store.core.definition.value.ExpressionValue;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

/**
 * @author Jacky.gao
 * @since 2016年12月30日
 */
@Component
public class ExpressionValueParser extends ValueParser{
	@Override
	public ExpressionValue parse(Element element) {
		ExpressionValue value=new ExpressionValue(element.getText());
		return value;
	}

	@Override
	public String getName() {
		return "expression-value";
	}
}
