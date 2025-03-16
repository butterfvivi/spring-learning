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

import org.vivi.framework.ureport.simple.ureport.definition.ConditionCellStyle;
import org.vivi.framework.ureport.simple.ureport.definition.ConditionPropertyItem;
import org.vivi.framework.ureport.simple.ureport.definition.LinkParameter;
import org.vivi.framework.ureport.simple.ureport.expression.ExpressionUtils;
import org.vivi.framework.ureport.simple.ureport.expression.model.Condition;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.Op;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BaseCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BothExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.CurrentValueExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.Join;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.PropertyExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

/**
 * @author Jacky.gao
 * @since 2017年4月11日
 */
public class ConditionParameterItemParser implements Parser<ConditionPropertyItem> {
	
	public final static Parser<ConditionPropertyItem> instance = new ConditionParameterItemParser();

	@Override
	public ConditionPropertyItem parse(Element element) {
		ConditionPropertyItem item = new ConditionPropertyItem();
		String rowHeight = element.attributeValue("row-height");
		if (StringUtils.isNotBlank(rowHeight)) {
			item.setRowHeight(Integer.valueOf(rowHeight));
		}
		String colWidth = element.attributeValue("col-width");
		if (StringUtils.isNotBlank(colWidth)) {
			item.setColWidth(Integer.valueOf(colWidth));
		}
		item.setName(element.attributeValue("name"));
		item.setNewValue(element.attributeValue("new-value"));
		item.setLinkUrl(element.attributeValue("link-url"));
		item.setLinkTargetWindow(element.attributeValue("link-target-window"));
		List<LinkParameter> parameters = null;
		List<Condition> conditions = new ArrayList<Condition>();
		item.setConditions(conditions);
		BaseCondition topCondition = null;
		BaseCondition prevCondition = null;
		for (Object obj : element.elements()) {
			if (obj == null || !(obj instanceof Element)) {
				continue;
			}
			Element ele = (Element) obj;
			String name = ele.getName();
			if ("condition".equals(name)) {
				BaseCondition condition = parseCondition(ele);
				conditions.add(condition);
				if (topCondition == null) {
					topCondition = condition;
					prevCondition = condition;
				} else {
					prevCondition.setNextCondition(condition);
					prevCondition.setJoin(condition.getJoin());
					prevCondition = condition;
				}
			} else if (CellStyleParser.instance.support(name)) {
				item.setCellStyle((ConditionCellStyle) CellStyleParser.instance.parse(ele));
			} else if (LinkParameterParser.instance.support(name)) {
				if (parameters == null) {
					parameters = new ArrayList<LinkParameter>();
				}
				parameters.add(LinkParameterParser.instance.parse(ele));
			} else if (ConditionPagingParser.instance.support(name)) {
				item.setPaging(ConditionPagingParser.instance.parse(ele));
			}
		}
		item.setCondition(topCondition);
		item.setLinkParameters(parameters);
		return item;
	}

	private BaseCondition parseCondition(Element ele) {
		String type = ele.attributeValue("type");
		if (type == null || "property".equals(type)) {
			PropertyExpressionCondition condition = new PropertyExpressionCondition();
			String property = ele.attributeValue("property");
			condition.setLeftProperty(property);
			condition.setLeft(property);
			String operation = ele.attributeValue("op");
			condition.setOperation(operation);
			condition.setOp(Op.parse(operation));
			for (Object o : ele.elements()) {
				if (o == null || !(o instanceof Element)) {
					continue;
				}
				Element e = (Element) o;
				if (!e.getName().equals("value")) {
					continue;
				}
				String expr = e.getTextTrim();
				condition.setRightExpression(ExpressionUtils.parseExpression(expr));
				condition.setRight(expr);
				break;
			}
			String join = ele.attributeValue("join");
			if (StringUtils.isNotBlank(join)) {
				condition.setJoin(Join.valueOf(join));
			}
			return condition;
		} else if ("current".equals(type)) {
			CurrentValueExpressionCondition condition = new CurrentValueExpressionCondition();
			String operation = ele.attributeValue("op");
			condition.setOperation(operation);
			condition.setOp(Op.parse(operation));
			for (Object o : ele.elements()) {
				if (o == null || !(o instanceof Element)) {
					continue;
				}
				Element e = (Element) o;
				if (!e.getName().equals("value")) {
					continue;
				}
				String expr = e.getTextTrim();
				condition.setRightExpression(ExpressionUtils.parseExpression(expr));
				condition.setRight(expr);
				break;
			}
			String join = ele.attributeValue("join");
			if (StringUtils.isNotBlank(join)) {
				condition.setJoin(Join.valueOf(join));
			}
			return condition;
		} else {
			BothExpressionCondition exprCondition = new BothExpressionCondition();
			exprCondition.setOperation(ele.attributeValue("op"));
			exprCondition.setOp(Op.parse(ele.attributeValue("op")));
			for (Object o : ele.elements()) {
				if (o == null || !(o instanceof Element)) {
					continue;
				}
				Element e = (Element) o;
				String name = e.getName();
				if (name.equals("left")) {
					String left = e.getText();
					Expression leftExpr = ExpressionUtils.parseExpression(left);
					exprCondition.setLeft(left);
					exprCondition.setLeftExpression(leftExpr);
				} else if (name.equals("right")) {
					String right = e.getText();
					Expression rightExpr = ExpressionUtils.parseExpression(right);
					exprCondition.setRight(right);
					exprCondition.setRightExpression(rightExpr);
				}
			}
			String join = ele.attributeValue("join");
			if (StringUtils.isNotBlank(join)) {
				exprCondition.setJoin(Join.valueOf(join));
			}
			return exprCondition;
		}
	}

	@Override
	public boolean support(String name) {
		return name.equals("condition-property-item");
	}
}
