package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.heiti;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class HeiTiFontRegister implements FontRegister {

    public String getFontName() {
        return "黑体";
    }

    public String getFontPath() {
        return path() + "heiti/SIMHEI.TTF";
    }
}
