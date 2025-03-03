package org.vivi.framework.report.service.handler;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.entity.PageEntity;
import org.vivi.framework.report.service.model.reportdatasource.ReportDatasource;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.MesGetSelectDataDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.MesReportDatasourceDto;
import org.vivi.framework.report.service.web.dto.reporttpldatasource.MesExecSqlDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IReportDatasourceService extends IService<ReportDatasource> {

    /**
     * @Title: tablePagingQuery
     * @Description: 表格分页查询
     */
    PageEntity tablePagingQuery(ReportDatasource model);

    /**
     *<p>Title: getDetail</p>
     *<p>Description: 获取详情</p>
     */
    BaseEntity getDetail(Long id);

    /**
     *<p>Title: insert</p>
     *<p>Description: 新增数据</p>
     */
    BaseEntity insert(ReportDatasource model);

    /**
     *<p>Title: update</p>
     *<p>Description: 更新数据</p>
     */
    BaseEntity update(ReportDatasource model);

    /**
     *<p>Title: delete</p>
     *<p>Description: 单条删除数据</p>
     */
    BaseEntity delete(Long id);

    /**
     *<p>Title: deleteBatch</p>
     *<p>Description: 批量删除数据</p>
     */
    BaseEntity deleteBatch(List<Long> ids);

    /**
     *<p>Title: getReportDatasource</p>
     *<p>Description: 获取数据源</p>
     */
    List<ReportDatasource> getReportDatasource(MesReportDatasourceDto mesReportDatasourceDto);

    /**
     *<p>Title: execSql</p>
     *<p>Description: </p>
     */
    List<Map<String, Object>> execSql(MesExecSqlDto mesExecSqlDto, UserInfoDto userInfoDto) throws SQLException, Exception;

    /**
     * @Title: connectionTest
     * @Description: 数据源连接测试
     */
    BaseEntity connectionTest(ReportDatasource reportDatasource);

    /**
     * @Title: getDatasourceSelectData
     * @Description: 获取下拉框数据

     */
    List<Map<String, Object>> getDatasourceSelectData(MesGetSelectDataDto mesGetSelectDataDto);

    /**
     * @MethodName: cacheDatasetsColumns
     * @Description: 缓存数据集列
     */
    void cacheDatasetsColumns(List<ReportTplDataset> datasets,UserInfoDto userInfoDto) throws SQLException, Exception;

    /**
     * @MethodName: getDatabseTables
     * @Description: 获取数据库的所有表信息
     */
    List<Map<String, String>> getDatabseTables(ReportDatasource datasource);

    /**
     * @MethodName: parseApiResultAttr
     * @Description: 解析api数据集结果属性
     */
    JSONArray parseApiResultAttr(ReportDatasource reportDatasource);

}
