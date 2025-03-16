package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.impact;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class ImpactFontRegister implements FontRegister {

    public String getFontName() {
        return "Impact";
    }

    public String getFontPath() {
        return path() + "impact/IMPACT.TTF";
    }
}
