package org.vivi.framework.ureport.store.core.export.pdf.font.impl.comicsansms;


import org.vivi.framework.ureport.store.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
@Component
public class ComicSansMSFontRegister implements FontRegister {

	public String getFontName() {
		return "Comic Sans MS";
	}

	public String getFontPath() {
		return path() + "comicsansms/COMIC.TTF";
	}
}
