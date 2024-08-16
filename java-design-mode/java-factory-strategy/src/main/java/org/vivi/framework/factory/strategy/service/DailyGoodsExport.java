package org.vivi.framework.factory.strategy.service;

import org.springframework.stereotype.Service;
import org.vivi.framework.factory.strategy.model.DailyGoods;
import org.vivi.framework.factory.strategy.strategy.ExportService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyGoodsExport implements ExportService {

    @Override
    public String getMode() {
        return "dailyGoods";
    }

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
