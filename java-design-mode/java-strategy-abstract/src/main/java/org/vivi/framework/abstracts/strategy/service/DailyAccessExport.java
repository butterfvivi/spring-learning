package org.vivi.framework.abstracts.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.abstracts.strategy.annotation.HandleType;
import org.vivi.framework.abstracts.strategy.model.DailyAccess;
import org.vivi.framework.abstracts.strategy.context.AbstractExportHandle;
import org.vivi.framework.abstracts.strategy.model.RequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@HandleType( value = "1")
public class DailyAccessExport extends AbstractExportHandle {


    @Override
    public List exportData(RequestDto param) {
        return initList().parallelStream()
                .peek(v -> v.setAccessId(v.getId() + v.getUserId()))
                .collect(Collectors.toList());
    }

    public List<DailyAccess> initList() {
        return new ArrayList<DailyAccess>() {
            {
                add(new DailyAccess(1L,1L,1L,"test01","test01@123","男"));
                add(new DailyAccess(2L,2L,2L,"test02","test02@123","女"));
                add(new DailyAccess(3L,3L,3L,"test03","test03@123","男"));
            }
        };
    }
}