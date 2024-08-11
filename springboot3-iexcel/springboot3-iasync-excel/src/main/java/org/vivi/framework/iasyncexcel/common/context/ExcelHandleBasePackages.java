package org.vivi.framework.iasyncexcel.common.context;

import java.util.Set;

public class ExcelHandleBasePackages {
    private Set<String> basePackages;

    public ExcelHandleBasePackages(Set<String> basePackages){
        this.basePackages=basePackages;
    }

    public Set<String> getBasePackages(){
        return basePackages;
    }
}

