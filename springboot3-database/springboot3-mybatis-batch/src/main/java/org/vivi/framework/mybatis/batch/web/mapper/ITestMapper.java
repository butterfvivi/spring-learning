package org.vivi.framework.mybatis.batch.web.mapper;

import org.apache.ibatis.annotations.*;
import org.vivi.framework.mybatis.batch.annotations.BatchInsert;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;

import java.util.List;

@Mapper
public interface ITestMapper {

    @Insert("insert into test (id, name) values (#{po.id}, #{po.name})")
    int insert(@Param("po") TestPO po);

    @Insert({"insert into test (id, name)", "values", "(#{po.id}, #{po.name})"})
    @BatchInsert(collection = "testPOS", item = "po", batchSize = 1000, flushStatements = true)
    void batchInsert(@Param("testPOS") List<TestPO> po);

    @BatchInsert(insert = "insert", collection = "testPOS", item = "po", batchSize = 10)
    int batchInsert2(@Param("testPOS") List<TestPO> po);

    @BatchInsert(collection = "testPOS", item = "po", batchSize = 10)
    @InsertProvider(type = Provider.class, method = "providerBatch")
    void providerBatchInsert(@Param("testPOS") List<TestPO> po);

    @BatchInsert(collection = "testPOS", item = "po", batchSize = 10)
    void xmlBatchInsert(@Param("testPOS") List<TestPO> po);

    void foreachInsert(@Param("testPOS") List<TestPO> po);

    @Insert({"<script>",
            "insert into test(id, name) values ",
            "<foreach collection='testPOS' index='index' item='po' separator=','>",
            "(#{po.id}, #{po.name})",
            "</foreach>",
            "</script>"})
    void forEachInsert(@Param("testPOS") List<TestPO> po);

    @Select("select * from test")
    List<TestPO> queryAll();

    void deleteAll();

    @Delete("delete from test")
    int deleteAll1();

    @Select("select count(*) from test")
    int count();

    class Provider {

        public String providerBatch(@Param("testPOS") List<TestPO> po) {
            return "insert into test (id, name) values (#{po.id}, #{po.name})";
        }

    }

}
