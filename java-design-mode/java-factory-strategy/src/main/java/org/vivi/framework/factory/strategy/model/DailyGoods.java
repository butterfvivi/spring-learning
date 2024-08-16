package org.vivi.framework.factory.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyGoods {

    private Long id;

    private String goodsName;

    private BigDecimal goodsPrice;

    private String goodsDesc;
}
