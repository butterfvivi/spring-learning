package org.vivi.framework.mybatis.batch.web.service;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;
import org.vivi.framework.mybatis.batch.web.mapper.ITestMapper;

import java.util.List;

@Repository
public class TestBatchDao {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public void batchInsert(List<TestPO> list) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            ITestMapper testMapper = sqlSession.getMapper(ITestMapper.class);
            for (int i = 0; i < list.size(); i++) {
                testMapper.insert(list.get(i));
                if (i % 10 == 9) {
                    sqlSession.flushStatements();
                }
                if (i > 55) {
                    sqlSession.rollback();
                    return;
                }
            }
            sqlSession.commit();
            sqlSession.clearCache();
        }
    }

}
