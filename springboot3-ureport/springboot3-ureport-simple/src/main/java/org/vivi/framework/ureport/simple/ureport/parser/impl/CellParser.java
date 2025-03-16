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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.CellDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionPropertyItem;
import org.vivi.framework.ureport.simple.ureport.definition.Expand;
import org.vivi.framework.ureport.simple.ureport.definition.LinkParameter;
import org.vivi.framework.ureport.simple.ureport.definition.value.ChartValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.DatasetValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.ExpressionValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.ImageValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.SimpleValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.SlashValue;
import org.vivi.framework.ureport.simple.ureport.definition.value.ZxingValue;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;
import org.vivi.framework.ureport.simple.ureport.expression.ExpressionUtils;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.ChartValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.DatasetValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.ExpressionValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.ImageValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.SimpleValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.SlashValueParser;
import org.vivi.framework.ureport.simple.ureport.parser.impl.value.ZxingValueParser;

/**
 * @author Jacky.gao
 * @since 2016年12月5日
 */
public class CellParser implements Parser<CellDefinition> {
	
	public final static Parser<CellDefinition> instance = new CellParser();
	
	@Override
	public CellDefinition parse(Element element) {
		CellDefinition cell = new CellDefinition();
		cell.setName(element.attributeValue("name"));
		cell.setColumnNumber(Integer.valueOf(element.attributeValue("col")));
		cell.setRowNumber(Integer.valueOf(element.attributeValue("row")));

		cell.setLeftParentCellName(element.attributeValue("left-cell"));
		cell.setTopParentCellName(element.attributeValue("top-cell"));

		String rowSpan = element.attributeValue("row-span");
		if (StringUtils.isNotBlank(rowSpan)) {
			cell.setRowSpan(Integer.valueOf(rowSpan));
		}
		String colSpan = element.attributeValue("col-span");
		if (StringUtils.isNotBlank(colSpan)) {
			cell.setColSpan(Integer.valueOf(colSpan));
		}
		String expand = element.attributeValue("expand");
		if (StringUtils.isNotBlank(expand)) {
			cell.setExpand(Expand.valueOf(expand));
		}
		String fillBlankRows = element.attributeValue("fill-blank-rows");
		if (StringUtils.isNotBlank(fillBlankRows)) {
			cell.setFillBlankRows(Boolean.valueOf(fillBlankRows));
			String multiple = element.attributeValue("multiple");
			if (StringUtils.isNotBlank(multiple)) {
				cell.setMultiple(Integer.valueOf(multiple));
			}
		}
		cell.setLinkTargetWindow(element.attributeValue("link-target-window"));
		String linkUrl = element.attributeValue("link-url");
		cell.setLinkUrl(linkUrl);
		if (StringUtils.isNotBlank(linkUrl)) {
			if (linkUrl.startsWith(ExpressionUtils.EXPR_PREFIX) && linkUrl.endsWith(ExpressionUtils.EXPR_SUFFIX)) {
				String expr = linkUrl.substring(2, linkUrl.length() - 1);
				Expression urlExpression = ExpressionUtils.parseExpression(expr);
				cell.setLinkUrlExpression(urlExpression);
			}
		}
		List<LinkParameter> linkParameters = null;
		List<ConditionPropertyItem> conditionPropertyItems = null;
		for (Object obj : element.elements()) {
			if (!(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String eleName = ele.getName();
			if(SimpleValueParser.instance.support(eleName)) {
				SimpleValue value = SimpleValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(ImageValueParser.instance.support(eleName)) {
				ImageValue value = ImageValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(ExpressionValueParser.instance.support(eleName)) {
				ExpressionValue value = ExpressionValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(DatasetValueParser.instance.support(eleName)) {
				DatasetValue value = DatasetValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(SlashValueParser.instance.support(eleName)) {
				SlashValue value = SlashValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(ZxingValueParser.instance.support(eleName)) {
				ZxingValue value = ZxingValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(ChartValueParser.instance.support(eleName)) {
				ChartValue value = ChartValueParser.instance.parse(ele);
				cell.setValue(value);
				continue;
			} 
			if(CellStyleParser.instance.support(eleName)) {
				cell.setCellStyle(CellStyleParser.instance.parse(ele));
				continue;
			} 
			if(LinkParameterParser.instance.support(eleName)) {
				if (linkParameters == null) {
					linkParameters = new ArrayList<LinkParameter>();
				}
				linkParameters.add(LinkParameterParser.instance.parse(ele));
				continue;
			} 
			if(ConditionParameterItemParser.instance.support(eleName)) {
				if (conditionPropertyItems == null) {
					conditionPropertyItems = new ArrayList<ConditionPropertyItem>();
				}
				conditionPropertyItems.add(ConditionParameterItemParser.instance.parse(ele));
				continue;
			}
		}
		if (linkParameters != null) {
			cell.setLinkParameters(linkParameters);
		}
		cell.setConditionPropertyItems(conditionPropertyItems);
		if (cell.getValue() == null) {
			throw new ReportException("Cell [" + cell.getName() + "] value not define.");
		}
		return cell;
	}

	@Override
	public boolean support(String name) {
		return name.equals("cell");
	}
}
