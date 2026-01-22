package org.vivi.framework.elasticsearch.simple.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.vivi.framework.elasticsearch.simple.model.Employee;
import org.vivi.framework.elasticsearch.simple.model.EmployeeQueryParameter;

import java.util.List;

/**
 *
 */
@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, Long> {

    List<Employee> findByName(String name);

    /**
     * 根据多个ID查询员工列表
     * @param ids
     * @return
     */
    //等同于：
    // {
    //  "query": {
    //    "ids": {
    //      "values": ["id1", "id2", "id3"]
    //    }
    //  }
    //}
    @Query("{\"ids\": {\"values\": ?0 }}")
    List<Employee> findByIds(List<Long> ids);

    //等同于
    // {
    //  "query": {
    //    "match": {
    //      "address": {
    //        "query": "广州"
    //      }
    //    }
    //  }
    //}
    @Query("{\"match\": {\"address\": {\"query\": \"?0\"}}}")
    Page<Employee> findByAddress(String address, Pageable pageable);

    @Query("""
            {
              "bool":{
                "must":[
                  {
                    "term":{
                      "name": "#{#name}"
                    }
                  }
                ]
              }
            }
            """)
    Page<Employee> findByName(String name, Pageable pageable);

    @Query("""
            {
              "bool":{
                "must":[
                  {
                    "term":{
                      "name": "#{#parameter.keyword}"
                    }
                  }
                ]
              }
            }
            """)
    Page<Employee> findByName(EmployeeQueryParameter parameter, Pageable pageable);

}
