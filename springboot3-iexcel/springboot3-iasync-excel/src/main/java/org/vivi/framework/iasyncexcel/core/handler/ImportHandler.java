package org.vivi.framework.iasyncexcel.core.handler;

import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.core.importer.ImportDto;

import java.util.List;

public interface ImportHandler<T> extends Handler {

    /**导入数据
     * @param list
     * @param param
     * @return
     * @throws Exception
     */
    List<ImportDto> importData(List<T> list, ImportDataParam param);
}
