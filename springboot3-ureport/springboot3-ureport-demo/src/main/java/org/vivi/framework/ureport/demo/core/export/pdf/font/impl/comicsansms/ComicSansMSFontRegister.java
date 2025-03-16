package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.comicsansms;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class ComicSansMSFontRegister implements FontRegister {

    public String getFontName() {
        return "Comic Sans MS";
    }

    public String getFontPath() {
        return path() + "comicsansms/COMIC.TTF";
    }
}
