package org.vivi.framework.ureport.simple.ureport.font.arial;

import org.vivi.framework.ureport.simple.ureport.font.FontRegister;


/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ArialFontRegister implements FontRegister {

	public String getFontName() {
		return "Arial";
	}

	public String getFontPath() {
		return "arial/ARIAL.TTF";
	}
}
