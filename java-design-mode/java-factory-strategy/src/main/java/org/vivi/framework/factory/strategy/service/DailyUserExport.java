package org.vivi.framework.factory.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.factory.strategy.model.DailyUser;
import org.vivi.framework.factory.strategy.strategy.ExportService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyUserExport implements ExportService {

    @Override
    public String getMode() {
        return "dailyUser";
    }

    @Override
    public List exportData() {
        return initList().parallelStream()
                .collect(Collectors.toList());
    }

    public List<DailyUser> initList() {
        return new ArrayList<DailyUser>() {
            {
                add(new DailyUser(1L,"vivi01","test01","test01@123","女"));
                add(new DailyUser(2L,"vivi02","test02","test02@123","男"));
                add(new DailyUser(3L,"vivi03","test03","test03@123","女"));
            }
        };
    }
}
