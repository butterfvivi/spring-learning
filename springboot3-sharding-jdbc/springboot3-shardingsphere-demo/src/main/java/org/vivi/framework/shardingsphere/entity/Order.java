package org.vivi.framework.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@TableName("t_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @TableId(type = IdType.NONE)

    private Long id =-1L;

    private String orderNo;

    private long userId;

    private BigDecimal amount;

}
