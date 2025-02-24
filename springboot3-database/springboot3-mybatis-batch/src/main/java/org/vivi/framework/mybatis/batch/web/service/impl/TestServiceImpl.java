package org.vivi.framework.mybatis.batch.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;
import org.vivi.framework.mybatis.batch.web.mapper.ITestMapper;
import org.vivi.framework.mybatis.batch.web.service.TestService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ITestMapper testMapper;

    @Override
    @Transactional
    public void test() {
        testMapper.deleteAll();
        int size = 105;
        List<TestPO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TestPO po = new TestPO();
            po.setId(i + 1);
            po.setName("yeemin");
            list.add(po);
        }
        testMapper.batchInsert(list);
        TestPO testPO = new TestPO();
        testPO.setId(106);
        testPO.setName("yeemin");
        testMapper.insert(testPO);
        System.out.println("count: " + testMapper.count());
        testMapper.deleteAll();
        testMapper.batchInsert2(list);
        testMapper.insert(testPO);
        System.out.println("count: " + testMapper.count());
    }

    @Override
    public int count() {
        return testMapper.count();
    }

}
