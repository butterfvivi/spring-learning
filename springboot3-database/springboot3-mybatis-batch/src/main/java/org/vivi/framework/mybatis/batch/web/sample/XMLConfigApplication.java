package org.vivi.framework.mybatis.batch.web.sample;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.vivi.framework.mybatis.batch.utils.SimpleSampleUtils;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;
import org.vivi.framework.mybatis.batch.web.mapper.ITestMapper;

import java.util.ArrayList;
import java.util.List;

public class XMLConfigApplication {


    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = SimpleSampleUtils.getSqlSessionFactory("h2");
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ITestMapper testMapper = sqlSession.getMapper(ITestMapper.class);
            testMapper.deleteAll();
            sqlSession.commit();

            int size = 100;
            List<TestPO> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TestPO po = new TestPO();
                po.setId(i + 1);
                po.setName("yeemin-" + po.getId());
                list.add(po);
            }
            long start = System.currentTimeMillis();
            testMapper.batchInsert(list);
            System.out.println("batch: " + (System.currentTimeMillis() - start));
            System.out.println("count: " + testMapper.count());
        }
    }

}
