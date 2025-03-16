package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.couriernew;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;

@Component
public class CourierNewFontRegister implements FontRegister {

    public String getFontName() {
        return "Courier New";
    }

    public String getFontPath() {
        return path() + "couriernew/COUR.TTF";
    }
}
