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
package org.vivi.framework.ureport.store.core.chart.axes;

import org.vivi.framework.ureport.store.core.chart.FontStyle;

/**
 * @author Jacky.gao
 * @since 2017年6月14日
 */
public class ScaleLabel {
	private boolean display=false;
	private String labelString="";
	private String fontColor="#666";
	private int fontSize=12;
	private FontStyle fontStyle= FontStyle.normal;
	
	public String toJson(){
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append("\"display\":"+display+",");
		sb.append("\"labelString\":\""+labelString+"\",");
		sb.append("\"fontColor\":\""+fontColor+"\",");
		sb.append("\"fontSize\":"+fontSize+",");
		sb.append("\"fontStyle\":\""+fontStyle+"\"");
		sb.append("}");
		return sb.toString();
	}
	
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public String getLabelString() {
		return labelString;
	}
	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public FontStyle getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}
}
