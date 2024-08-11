package org.vivi.framework.iasyncexcel.excel.importer;

import java.util.List;

public interface ImportHandler<T> {

    /**导入数据
     * @param list
     * @param param
     * @return
     * @throws Exception
     */
    List<ImportDto> importData(List<T> list, ImportDataParam param);
}
