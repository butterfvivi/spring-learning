package org.vivi.framework.iexcelsimple.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexcelsimple.entity.dto.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    int insertBatch(List<Item> items);
}
