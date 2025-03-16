package org.vivi.framework.ureport.simple.ureport.parser.impl;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.FreezeCellDefinition;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

public class FreezeCellParser implements Parser<FreezeCellDefinition>{
	
	public final static Parser<FreezeCellDefinition> instance = new FreezeCellParser();

	@Override
	public FreezeCellDefinition parse(Element element) {
		FreezeCellDefinition freeze = new FreezeCellDefinition();
		String colIndex = element.attributeValue("col");
		if(StringUtils.isNotBlank(colIndex)) {
			freeze.setCol(Integer.valueOf(colIndex));
		}
		String rowIndex = element.attributeValue("row");
		if(StringUtils.isNotBlank(rowIndex)) {
			freeze.setRow(Integer.valueOf(rowIndex));
		}
		return freeze;
	}

	@Override
	public boolean support(String name) {
		return name.equals("freeze");
	}
}
