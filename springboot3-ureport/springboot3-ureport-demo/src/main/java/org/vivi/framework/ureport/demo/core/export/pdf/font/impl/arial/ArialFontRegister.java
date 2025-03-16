package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.arial;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class ArialFontRegister implements FontRegister {

    public String getFontName() {
        return "Arial";
    }

    public String getFontPath() {
        return path() + "arial/ARIAL.TTF";
    }
}
