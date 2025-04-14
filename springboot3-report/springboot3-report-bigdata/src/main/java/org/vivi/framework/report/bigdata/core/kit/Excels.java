package org.vivi.framework.report.bigdata.core.kit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Excels {

    public static Export Export(){
        return Export.create();
    }

    public static Import Import(){
        return Import.create();
    }
}
