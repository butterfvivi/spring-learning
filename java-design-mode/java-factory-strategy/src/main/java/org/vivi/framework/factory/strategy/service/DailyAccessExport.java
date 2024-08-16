package org.vivi.framework.factory.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.factory.strategy.model.DailyAccess;
import org.vivi.framework.factory.strategy.strategy.ExportService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyAccessExport implements ExportService {

    @Override
    public String getMode() {
        return "dailyAccess";
    }

    @Override
    public List exportData() {
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
