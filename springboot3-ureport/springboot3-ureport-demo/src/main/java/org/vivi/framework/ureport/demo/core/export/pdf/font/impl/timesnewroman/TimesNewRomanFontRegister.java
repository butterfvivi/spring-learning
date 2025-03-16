package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.timesnewroman;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class TimesNewRomanFontRegister implements FontRegister {

    public String getFontName() {
        return "Times New Roman";
    }

    public String getFontPath() {
        return path() + "timesnewroman/TIMES.TTF";
    }
}
