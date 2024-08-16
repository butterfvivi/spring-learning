package org.vivi.framework.abstracts.strategy.context;

import org.vivi.framework.abstracts.strategy.model.RequestDto;

public interface ExportHandle {

    Object exportData(RequestDto param);
}
