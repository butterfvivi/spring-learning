package org.vivi.framework.ureport.simple.ureport.parser.impl;

import org.dom4j.Element;

import org.vivi.framework.ureport.simple.ureport.definition.FloatImage;
import org.vivi.framework.ureport.simple.ureport.parser.Parser;

public class FloatImageParser implements Parser<FloatImage>{
	
	public final static Parser<FloatImage> instance = new FloatImageParser();
	
	@Override
	public FloatImage parse(Element element) {
		FloatImage image = new FloatImage();
		
		Integer width = Integer.parseInt(element.attributeValue("width"));
		image.setWidth(width);
		
		Integer height = Integer.parseInt(element.attributeValue("height"));
		image.setHeight(height);
		
		Integer top = Integer.parseInt(element.attributeValue("top"));
		image.setTop(top);
		
		Integer left = Integer.parseInt(element.attributeValue("left"));
		image.setLeft(left);
		
		image.setOnlyFirstPage(Boolean.valueOf(element.attributeValue("type")));
		
		String value = element.getText();
		image.setValue(value);
		return image;
	}
	
	@Override
	public boolean support(String name) {
		return name.equals("image");
	}
}
