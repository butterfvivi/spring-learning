package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.fangsong;


import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class FangSongFontRegister implements FontRegister {

    public String getFontName() {
        return "仿宋";
    }

    public String getFontPath() {
        return path() + "fangsong/SIMFANG.TTF";
    }
}
