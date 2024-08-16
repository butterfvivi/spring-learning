package org.vivi.framework.abstracts.strategy.context;

import org.vivi.framework.abstracts.strategy.model.RequestDto;

public abstract class AbstractExportHandle implements ExportHandle {

    abstract public Object exportData(RequestDto param);
}
