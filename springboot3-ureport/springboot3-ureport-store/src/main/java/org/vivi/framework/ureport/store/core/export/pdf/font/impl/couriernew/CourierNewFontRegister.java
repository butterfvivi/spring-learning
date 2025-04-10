package org.vivi.framework.ureport.store.core.export.pdf.font.impl.couriernew;


import org.vivi.framework.ureport.store.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
@Component
public class CourierNewFontRegister implements FontRegister {

	public String getFontName() {
		return "Courier New";
	}

	public String getFontPath() {
		return path() + "couriernew/COUR.TTF";
	}
}
