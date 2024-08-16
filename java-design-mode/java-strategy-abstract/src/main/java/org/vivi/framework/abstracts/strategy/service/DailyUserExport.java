package org.vivi.framework.abstracts.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.abstracts.strategy.annotation.HandleType;
import org.vivi.framework.abstracts.strategy.model.DailyUser;
import org.vivi.framework.abstracts.strategy.context.AbstractExportHandle;
import org.vivi.framework.abstracts.strategy.model.RequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@HandleType( value = "3")
public class DailyUserExport extends AbstractExportHandle {


    @Override
    public List exportData(RequestDto param) {
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
