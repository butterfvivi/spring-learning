package org.vivi.framework.mybatis.batch.web.mapper;

import org.apache.ibatis.annotations.*;
import org.vivi.framework.mybatis.batch.annotations.BatchInsert;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;

import java.util.Date;
import java.util.List;

@Mapper
public interface OTestMapper {

    @Select("select sysdate from dual")
    Date getSysdate();

    @Insert("insert into test (id, name) values (#{po.id}, #{po.name})")
    int insert(@Param("po") TestPO po);

    @Insert({"insert into test (id, name)", "values", "(#{po.id}, #{po.name})"})
    @BatchInsert(collection = "testPOS", item = "po", batchSize = 10, flushStatements = false)
    void batchInsert(@Param("testPOS") List<TestPO> po);

    @BatchInsert(insert = "insert", collection = "testPOS", item = "po", batchSize = 10)
    int batchInsert2(@Param("testPOS") List<TestPO> po);

    @Delete("truncate table test")
    void deleteAll();

    @Select("select count(*) from test")
    int count();
}
