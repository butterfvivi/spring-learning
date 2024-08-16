package org.vivi.framework.abstracts.strategy.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.vivi.framework.abstracts.strategy.context.ExportHandle;
import org.vivi.framework.abstracts.strategy.context.HandleContext;
import org.vivi.framework.abstracts.strategy.model.RequestDto;

@Service
public class ExportService implements ExportHandle {

    @Resource
    private HandleContext handlerContext;

    @Override
    public Object exportData(RequestDto param) {
        ExportHandle instance = handlerContext.getInstance(param);
        return instance.exportData(param);
    }
}
