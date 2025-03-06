package org.vivi.framework.easyexcel.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.easyexcel.simple.model.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
