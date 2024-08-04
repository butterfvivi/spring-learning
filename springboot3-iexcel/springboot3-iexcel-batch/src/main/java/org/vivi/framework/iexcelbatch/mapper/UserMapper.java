package org.vivi.framework.iexcelbatch.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iexcelbatch.common.mybatis.SpiceBaseMapper;
import org.vivi.framework.iexcelbatch.entity.model.User;

import java.util.List;

@Mapper
public interface UserMapper extends SpiceBaseMapper<User> {

    int insertBatch(List<User> list);

}
