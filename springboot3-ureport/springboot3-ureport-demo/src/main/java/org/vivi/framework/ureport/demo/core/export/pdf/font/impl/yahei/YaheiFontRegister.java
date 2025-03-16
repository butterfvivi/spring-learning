package org.vivi.framework.ureport.demo.core.export.pdf.font.impl.yahei;

import org.vivi.framework.ureport.demo.core.export.pdf.font.FontRegister;
import org.springframework.stereotype.Component;

@Component
public class YaheiFontRegister implements FontRegister {

    public String getFontName() {
        return "微软雅黑";
    }

    public String getFontPath() {
        return path() + "fangsong/SIMFANG.TTF";
    }
}
