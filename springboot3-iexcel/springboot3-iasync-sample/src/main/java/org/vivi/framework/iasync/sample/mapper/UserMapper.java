package org.vivi.framework.iasync.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.iasync.sample.model.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    int insertBatch(List<User> list);

}
