package org.vivi.framework.ureport.simple.ureport.font.timesnewroman;

import org.vivi.framework.ureport.simple.ureport.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class TimesNewRomanFontRegister implements FontRegister {

	public String getFontName() {
		return "Times New Roman";
	}

	public String getFontPath() {
		return "timesnewroman/TIMES.TTF";
	}
}
