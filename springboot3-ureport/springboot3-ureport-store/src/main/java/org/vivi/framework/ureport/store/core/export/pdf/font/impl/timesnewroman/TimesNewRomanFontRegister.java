package org.vivi.framework.ureport.store.core.export.pdf.font.impl.timesnewroman;


import org.vivi.framework.ureport.store.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
@Component
public class TimesNewRomanFontRegister implements FontRegister {

	public String getFontName() {
		return "Times New Roman";
	}

	public String getFontPath() {
		return path() + "timesnewroman/TIMES.TTF";
	}
}
