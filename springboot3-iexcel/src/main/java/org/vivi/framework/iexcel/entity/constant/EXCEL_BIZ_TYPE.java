package org.vivi.framework.iexcel.entity.constant;


import org.vivi.framework.iexcel.entity.excel.CityExcel;
import org.vivi.framework.iexcel.common.read.BasedExcelReadModel;

public enum EXCEL_BIZ_TYPE {
    CITY(CityExcel.class);

    public final Class<? extends BasedExcelReadModel> excelClazz;

    EXCEL_BIZ_TYPE(Class<? extends BasedExcelReadModel> excelClazz) {
        this.excelClazz = excelClazz;
    }
}
