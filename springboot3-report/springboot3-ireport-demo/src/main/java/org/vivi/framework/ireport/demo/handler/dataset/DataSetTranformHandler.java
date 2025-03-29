package org.vivi.framework.ireport.demo.handler.dataset;

import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.process.JdbcProcess;

import java.util.List;
import java.util.Map;

@Service
public class DataSetTranformHandler {

    public List<Map<String, Object>>  getDataSet(String sql, Map<String, Object> params) {
        List<Map<String, Object>> result = null;
        //result = JdbcProcess.parseMetaDataColumns(dataSource, mesExecSqlDto.getTplSql(),reportDatasource.getType(),mesExecSqlDto.getSqlParams());
        return null;
    }
}
