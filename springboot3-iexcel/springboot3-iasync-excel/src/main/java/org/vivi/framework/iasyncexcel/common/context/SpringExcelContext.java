package org.vivi.framework.iasyncexcel.common.context;

public class SpringExcelContext extends ExcelContextFactory {

    public SpringExcelContext() {
        super(ExcelContextConfiguration.class, "excelContext", "spring.excel.name");
    }
}
