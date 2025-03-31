
package org.vivi.framework.report.simple.modules.datasource.service;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.simple.web.dto.DataSetDto;
import org.vivi.framework.report.simple.modules.datasource.entity.DataSource;
import org.vivi.framework.report.simple.modules.datasource.entity.dto.DataSourceDto;
import org.vivi.framework.report.simple.modules.datasource.entity.param.ConnectionParam;

import java.util.List;

/**
* @desc DataSource 数据集服务接口
**/
public interface DataSourceService{

    /**
     * 获取所有数据源
     * @return
     */
    List<DataSource> queryAllDataSource();

    /**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    Boolean testConnection(ConnectionParam connectionParam);

    /**
     * 执行sql
     * @param dto
     * @return
     */
    List<JSONObject> execute(DataSourceDto dto);

    /**
     * 执行sql,统计数据total
     * @param dataSourceDto
     * @param dto
     * @return
     */
    long total(DataSourceDto dataSourceDto, DataSetDto dto);
}
