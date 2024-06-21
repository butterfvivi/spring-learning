package org.vivi.framework.easyexcelsimple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.vivi.framework.easyexcelsimple.model.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
