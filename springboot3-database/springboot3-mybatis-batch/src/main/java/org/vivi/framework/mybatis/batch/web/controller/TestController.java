package org.vivi.framework.mybatis.batch.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.mybatis.batch.web.entity.TestPO;
import org.vivi.framework.mybatis.batch.web.mapper.ITestMapper;
import org.vivi.framework.mybatis.batch.web.service.TestBatchDao;
import org.vivi.framework.mybatis.batch.web.service.TestService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mysql")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ITestMapper testMapper;

    @Autowired
    private TestBatchDao testBatchDao;

    @RequestMapping("/test")
    public String test() {
        testService.test();
        return testService.count() + "";
    }

    @RequestMapping("/insert")
    public void insert() {
        testMapper.deleteAll();
        TestPO po = new TestPO();
        po.setId(1);
        po.setName("yeemin");
        testMapper.insert(po);
        System.out.println(testMapper.queryAll());
    }

    @RequestMapping("/queryAll")
    public void queryAll() {
        List<TestPO> list = testMapper.queryAll();
        System.out.println(testMapper.count());
        System.out.println(list);
    }

    @RequestMapping("/deleteAll")
    public void deleteAll() {
        testMapper.deleteAll();
    }

    @RequestMapping("/batchInsert")
    public void batchInsert() {
        testMapper.deleteAll();
        List<TestPO> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestPO po = new TestPO();
            po.setId(i + 1);
            po.setName("yeemin");
            list.add(po);
        }
        testBatchDao.batchInsert(list);
        list = testMapper.queryAll();
        System.out.println(list);
    }

    @RequestMapping("/batchInsertMapper")
    @Transactional
    public void batchInsertMapper() {
        testMapper.deleteAll();
        int size = 101;
        List<TestPO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TestPO po = new TestPO();
            po.setId(i + 1);
            po.setName("yeemin");
            list.add(po);
        }
        long start = System.currentTimeMillis();
        testMapper.batchInsert(list);
        deleteAll();
        testMapper.providerBatchInsert(list);
        deleteAll();
        testMapper.xmlBatchInsert(list);
        System.out.println("batch: " + (System.currentTimeMillis() - start));
        System.out.println("count: " + testMapper.count());
    }


    @RequestMapping("/batchInsertMapper3")
    public void batchInsertMapper3() {
        testMapper.deleteAll();
        List<TestPO> list = new ArrayList<>();
        int size = 10000000;
        for (int i = 0; i < size; i++) {
            TestPO po = new TestPO();
            po.setId(i + 1);
            po.setName("yeemin");
            list.add(po);
        }
        long start = System.currentTimeMillis();
        int count = 0;
        while (count + 500 < size) {
            testMapper.forEachInsert(list.subList(count, count + 500));
            count += 500;
        }
        if (count < size) {
            testMapper.forEachInsert(list.subList(count, size));
        }
        System.out.println("foreach: " + (System.currentTimeMillis() - start));
//		System.out.println(testMapper.queryAll());
    }

    @RequestMapping("/batchInsertMapper2")
    public void batchInsertMapper2() {
        testMapper.deleteAll();
        List<TestPO> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestPO po = new TestPO();
            po.setId(i + 1);
            po.setName("yeemin");
            list.add(po);
        }
        try {
            testMapper.batchInsert2(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(testMapper.count());
    }

    @RequestMapping("/transactionTest")
    public void transactionTest() {
        testMapper.deleteAll();
        testService.test();
        System.out.println(testService.count());
    }

}
