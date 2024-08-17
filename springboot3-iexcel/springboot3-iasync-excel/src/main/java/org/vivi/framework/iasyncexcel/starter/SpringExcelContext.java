package org.vivi.framework.iasyncexcel.starter;

public class SpringExcelContext extends ExcelContextFactory {

    public SpringExcelContext() {
        super(ExcelContextConfiguration.class, "excelContext", "spring.excel.name");
    }
}
