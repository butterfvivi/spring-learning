package org.vivi.framework.factory.service;

import org.vivi.framework.factory.model.DailyGoods;
import org.vivi.framework.factory.strategy.ExportService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DailyGoodsExport implements ExportService {

    @Override
    public List exportData() {
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
