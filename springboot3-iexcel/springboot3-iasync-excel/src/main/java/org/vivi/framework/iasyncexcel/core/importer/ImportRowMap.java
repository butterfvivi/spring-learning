package org.vivi.framework.iasyncexcel.core.importer;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.metadata.Cell;
import lombok.Data;

import java.util.Map;

@Data
public class ImportRowMap {

    @ExcelIgnore
    private Map<Integer,String> headMap;

    @ExcelIgnore
    private Map<Integer, Cell> dataMap;
}
