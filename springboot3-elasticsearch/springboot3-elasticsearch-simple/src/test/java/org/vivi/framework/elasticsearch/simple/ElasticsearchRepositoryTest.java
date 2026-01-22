package org.vivi.framework.elasticsearch.simple;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.vivi.framework.elasticsearch.simple.model.Employee;
import org.vivi.framework.elasticsearch.simple.model.EmployeeQueryParameter;
import org.vivi.framework.elasticsearch.simple.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ElasticsearchRepositoryTest extends ElasticsearchSimpleApplicationTests {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void testFindById(){
        Employee employee = employeeRepository.findById(1L).orElse(null);
        log.info("employee:{}",employee);
    }

    @Test
    public void testSaveAndFind(){
        Employee employee = new Employee(7L,"vivi",0,20,"上海浦东新区","java");
        employeeRepository.save(employee);

        // 根据Id查询
        Employee employee1 = employeeRepository.findById(7L).orElse(null);
        log.info("employee1:{}",employee1);

        // 根据name查询
        employeeRepository.findByName("vivi").forEach(e -> log.info("find by name vivi: {}", e));
    }

    @Test
    public void testFindByIds(){
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        employeeRepository.findByIds(ids).forEach(e -> log.info("find by ids: {}", e));
    }

    @Test
    public void testFindByAdress(){
        employeeRepository.findByName("上海").forEach(e -> log.info("find by address 上海: {}", e));
    }

    @Test
    public void testFindByName(){
        employeeRepository.findByName("vivi", Pageable.ofSize(3)).forEach(e -> log.info("find by name vivi: {}", e));
    }

    @Test
    public void testFindByNameAndParameter(){
        EmployeeQueryParameter employeeQueryParameter = new EmployeeQueryParameter("李四");
        employeeRepository.findByName(employeeQueryParameter, Pageable.ofSize(3))
                .forEach(e -> log.info("find name {} by parameter: {}", employeeQueryParameter.keyword(), e));
    }
}
