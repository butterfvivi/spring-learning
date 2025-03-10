package org.vivi.framework.mybatis.batch.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;
import org.vivi.framework.mybatis.batch.web.mapper.ITestMapper;
import org.vivi.framework.mybatis.batch.web.mapper.OTestMapper;
import org.vivi.framework.mybatis.batch.web.service.OracleTestService;
import org.vivi.framework.mybatis.batch.web.service.TestService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OracleTestServiceImpl implements OracleTestService {

    @Autowired
    private OTestMapper testMapper;

    @Override
    public Date getSysdate() {
        return testMapper.getSysdate();
    }

    @Override
    public int count() {
        return testMapper.count();
    }

    @Override
    public int batch() {
        testMapper.deleteAll();
        List<TestPO> list = generateList(15);
        testMapper.batchInsert(list);
        int count = testMapper.count();
        // 设置flushStatements为false，最后一页没有预提交，所以count为10
        Assert.isTrue(count == 10, "");
        return count;
    }

    @Override
    public int batch2() {
        testMapper.deleteAll();
        List<TestPO> list = generateList(16);
        testMapper.batchInsert2(list);
        int count = testMapper.count();
        // 设置flushStatements默认为true，最后一页预提交，所以count为16
        Assert.isTrue(count == 16, "");
        return count;
    }

    @Override
    public String unique1() {
        testMapper.deleteAll();
        TestPO testPO = new TestPO();
        testPO.setId(16);
        testPO.setName("unique");
        // 第一个sqlSession保存了id为16的数据，但是没有预提交
        testMapper.insert(testPO);
        List<TestPO> list = generateList(17);
        try {
            // 第二个session感知不到
            testMapper.batchInsert(list);
        } catch (Exception e) {
            // 并不会走到这里
            return "batchInsert 执行期间发现了异常";
        }
        // 方法执行完，准备提交事务，但是发生了主键冲突，所有数据库操作回滚
        return "success";
    }

    @Override
    public String unique2() {
        testMapper.deleteAll();
        TestPO testPO = new TestPO();
        testPO.setId(16);
        testPO.setName("unique");
        // 第一个sqlSession保存了id为16的数据，但是预提交
        testMapper.insert(testPO);
        List<TestPO> list = generateList(17);
        try {
            // 第二个session感知到了相同的主键并抛出主键冲突的异常
            testMapper.batchInsert2(list);
        } catch (Exception e) {
            // 执行到插入id为16的数据时发现了异常，由于本方法捕获了异常，所以数据库有16条数据，其中id为16是第一个，后面依次是id从1到15
            return "batchInsert 执行期间发现了异常，异常发生时会话数据数：" + testMapper.count();
        }
        return "success";
    }

    @Override
    public String unique3() {
        testMapper.deleteAll();
        TestPO testPO = new TestPO();
        testPO.setId(16);
        testPO.setName("unique");
        // 第一个sqlSession保存了id为16的数据，但是预提交
        testMapper.insert(testPO);
        List<TestPO> list = generateList(17);
        try {
            // 第二个session感知到了相同的主键并抛出主键冲突的异常
            testMapper.batchInsert2(list);
        } catch (Exception e) {
            // 执行到插入id为16的数据时发现了异常，由于本方法继续抛出异常，所以事务回滚，数据库没有数据
            throw e;
        }
        return "success";
    }

    private List<TestPO> generateList(int size) {
        List<TestPO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TestPO testPO = new TestPO();
            testPO.setId(i + 1);
            testPO.setName("yeemin" + testPO.getId());
            list.add(testPO);
        }
        return list;
    }
}