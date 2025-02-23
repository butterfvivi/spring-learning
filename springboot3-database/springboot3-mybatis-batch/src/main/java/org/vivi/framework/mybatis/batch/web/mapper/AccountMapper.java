package org.vivi.framework.mybatis.batch.web.mapper;

import org.apache.ibatis.annotations.*;
import org.vivi.framework.mybatis.batch.annotations.BatchInsert;
import org.vivi.framework.mybatis.batch.web.entity.AccountEntity;

import java.util.Date;
import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("select sysdate from dual")
    Date getSysdate();

    @Insert("insert into test (`id`,`account_number`,`password`,`money`,`client_id`) values (#{account.id}, #{account.name})")
    int insert(@Param("account") AccountEntity account);

    @Insert({"insert into test (`id`,`account_number`,`password`,`money`,`client_id`)", "values", "(#{account.id}, #{account.name})"})
    @BatchInsert(collection = "testPOS", item = "account", batchSize = 10, flushStatements = false)
    void batchInsert(@Param("testPOS") List<AccountEntity> account);

    @BatchInsert(insert = "insert", collection = "testPOS", item = "account", batchSize = 10)
    int batchInsert2(@Param("testPOS") List<AccountEntity> account);

    @Delete("truncate table test")
    void deleteAll();

    @Select("select count(*) from test")
    int count();

}
