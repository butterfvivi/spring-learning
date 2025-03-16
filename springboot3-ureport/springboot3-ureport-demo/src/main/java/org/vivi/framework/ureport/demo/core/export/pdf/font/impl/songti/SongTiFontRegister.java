package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.songti;

import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;


@Component
public class SongTiFontRegister implements FontRegister {

    public String getFontName() {
        return "宋体";
    }

    public String getFontPath() {
        return path() + "songti/SIMSUN.TTC";
    }
}
