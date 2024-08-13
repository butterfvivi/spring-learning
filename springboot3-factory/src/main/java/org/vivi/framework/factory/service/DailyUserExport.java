package org.vivi.framework.factory.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.factory.model.DailyUser;
import org.vivi.framework.factory.strategy.ExportService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyUserExport implements ExportService {

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
