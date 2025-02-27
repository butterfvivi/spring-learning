package org.vivi.framework.dynamic.sqlbatis3.mapper;

import org.apache.ibatis.annotations.*;
import org.vivi.framework.dynamic.sqlbatis3.model.UserBaseDO;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("@UserMapper.insert")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insert(UserBaseDO userBaseDO);

    @Select(" <script> "+
            "select * from user\n" +
            " <where>\n" +
            "and user_name like CONCAT('%',#{userName},'%') \n" +
            "<if test=\"null!=createTime\"> \n" +
            "and create_time  &lt; #{createTime}\n" +
            "</if>\n" +
            "</where>"+
            " </script> ")
    List<UserBaseDO> findListOrg(@Param("userName") String userName,
                              @Param("createTime") LocalDateTime createTime);

    @Select("@UserMapper.findList")
    List<UserBaseDO> findList(@Param("userName") String userName,
                              @Param("createTime") LocalDateTime createTime);



}
