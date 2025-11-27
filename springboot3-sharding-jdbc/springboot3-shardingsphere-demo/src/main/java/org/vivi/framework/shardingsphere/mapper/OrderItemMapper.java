package org.vivi.framework.shardingsphere.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.shardingsphere.entity.OrderItem;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
