package org.vivi.framework.abstracts.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.abstracts.strategy.annotation.HandleType;
import org.vivi.framework.abstracts.strategy.model.DailyGoods;
import org.vivi.framework.abstracts.strategy.context.AbstractExportHandle;
import org.vivi.framework.abstracts.strategy.model.RequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@HandleType( value = "2")
public class DailyGoodsExport extends AbstractExportHandle {


    @Override
    public List exportData(RequestDto param) {
        return initList().parallelStream()
                .collect(Collectors.toList());
    }

    public List<DailyGoods> initList() {
        return new ArrayList<DailyGoods>() {
            {
                add(new DailyGoods(1L,"goods01",new BigDecimal("8000.01"),"goods_1"));
                add(new DailyGoods(2L,"goods02",new BigDecimal("3000.01"),"goods_2"));
                add(new DailyGoods(3L,"goods03",new BigDecimal("6000.3"),"goods_3"));
            }
        };
    }
}
