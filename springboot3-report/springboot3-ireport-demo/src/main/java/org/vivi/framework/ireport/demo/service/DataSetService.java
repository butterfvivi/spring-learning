package org.vivi.framework.ireport.demo.service;

import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ISqlMapper;
import org.vivi.framework.ireport.demo.process.JdbcProcess;

import java.util.List;
import java.util.Map;

@Service
public class DataSetService {

    @Autowired
    private ISqlMapper sqlMapper;

    public List<Map<String, Object>> getData(String sql, Map<String, Object> params)   {
        sql = JdbcProcess.processSqlParams(sql,params);
        return sqlMapper.selectList(sql);
    }

}
