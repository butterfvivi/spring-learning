package org.vivi.framework.seata.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.vivi.framework.seata.model.Account;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO account\n" +
      "(name, account)" +
              "VALUES(#{name}, #{account});")
    int insert(Account accountVo);

    @Update("UPDATE account " +
      "SET account=account+#{account}" +
              "WHERE name=#{name};")
    int add (Account accountVo);

}
