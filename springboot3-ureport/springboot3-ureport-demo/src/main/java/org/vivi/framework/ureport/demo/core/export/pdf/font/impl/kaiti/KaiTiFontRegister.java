package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.kaiti;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class KaiTiFontRegister implements FontRegister {

    public String getFontName() {
        return "楷体";
    }

    public String getFontPath() {
        return path() + "kaiti/SIMKAI.TTF";
    }
}
