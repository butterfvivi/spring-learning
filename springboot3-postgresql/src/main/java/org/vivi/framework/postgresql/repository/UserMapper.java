package org.vivi.framework.postgresql.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.vivi.framework.postgresql.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from public.pg_user where name=#{name} and age=#{age}")
    User findByNameAndAge(@Param("name")String name,@Param("age")Integer age);

    @Select(" select * from public.pg_user where name= #{name}")
    User findUser(@Param("name") String name);
}

