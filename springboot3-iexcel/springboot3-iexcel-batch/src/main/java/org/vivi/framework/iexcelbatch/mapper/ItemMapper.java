package org.vivi.framework.iexcelbatch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexcelbatch.entity.model.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    int insertBatch(List<Item> items);
}
