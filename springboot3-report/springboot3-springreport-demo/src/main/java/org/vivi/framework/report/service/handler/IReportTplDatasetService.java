package org.vivi.framework.report.service.handler;

import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.jsqlparser.JSQLParserException;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.entity.PageEntity;
import org.vivi.framework.report.service.model.reportdatasource.ReportDatasource;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;
import org.vivi.framework.report.service.model.reporttpldatasetgroup.ReportTplDatasetGroup;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.ApiTestResultDto;
import org.vivi.framework.report.service.web.dto.reporttpldataset.MesGetRelyOnSelectData;
import org.vivi.framework.report.service.web.dto.reporttpldataset.ReportDatasetDto;
import org.vivi.framework.report.service.web.dto.reporttpldataset.ReportTplDatasetDto;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IReportTplDatasetService extends IService<ReportTplDataset> {


    /**
     * @Title: tablePagingQuery
     * @Description: 表格分页查询
     * @param @param model
     * @return BaseEntity
     * @throws
     */
    PageEntity tablePagingQuery(ReportTplDataset model);

    /**
     *<p>Title: getDetail</p>
     *<p>Description: 获取详情</p>
     */
    BaseEntity getDetail(Long id);

    /**
     *<p>Title: insert</p>
     *<p>Description: 新增数据</p>
     */
    BaseEntity insert(ReportTplDataset model);

    /**
     *<p>Title: update</p>
     *<p>Description: 更新数据</p>
     */
    BaseEntity update(ReportTplDataset model);

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
     *<p>Title: getTplDatasets</p>
     *<p>Description: 获取报表模板关联的数据集</p>
     */
    List<ReportDatasetDto> getTplDatasets(ReportTplDataset dataset, UserInfoDto userInfoDto) throws SQLException, Exception;

    /**
     * @MethodName: getDatasetColumns
     * @Description: 获取数据集字段

     */
    List<Map<String, Object>> getDatasetColumns(ReportTplDataset dataset, UserInfoDto userInfoDto) throws SQLException, Exception;

    /**
     * @MethodName: getApiDefaultRequestResult
     * @Description: 获取api数据集默认参数请求结果
     */
    ApiTestResultDto getApiDefaultRequestResult(ReportTplDataset dataset);

    /**
     *<p>Title: addTplDataSets</p>
     *<p>Description: 报表模板添加数据集</p>
     */
    ReportDatasetDto addTplDataSets(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws SQLException, Exception;

    /**
     * @Title: getTplDatasetAndDatasource
     * @Description: 根据模板id和数据集名称获取数据集和数据源
     */
    Map<String, Object> getTplDatasetAndDatasource(Long tplId,String datasetName) throws SQLException, Exception;

    /**
     * @MethodName: getTplDatasetAndDatasource
     * @Description: 根据数据集id获取数据集和数据源
     */
    Map<String, Object> getTplDatasetAndDatasource(Long datasetId) throws SQLException, Exception;

    /**
     * @Title: getReportDatasetsParam
     * @Description: 获取报表数据集参数
     * @param reportTplDataset
     * @return
     * @author caiyang
     * @throws ParseException
     * @date 2021-06-03 02:24:46
     */
    Map<String, Object> getReportDatasetsParam(ReportTplDatasetDto reportTplDataset) throws ParseException;

    /**
     * @Title: getTplDatasetColumns

     */
    List<Map<String, Object>> getTplDatasetColumns(ReportTplDataset reportTplDataset,UserInfoDto userInfoDto) throws SQLException, Exception;


    /**
     * @MethodName: getRelyOnSelectData
     * @Description: 获取依赖项的下拉数据
     */
    List<Map<String, Object>> getRelyOnSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;

    /**
     * @MethodName: getSelectData
     * @Description: 获取下拉数据
     */
    List<Map<String, Object>> getSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;

    /**
     * @MethodName: getTreeSelectData
     * @Description: 获取下拉树数据
     */
    List<Map<String, Object>> getTreeSelectData(MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException;

    /**
     * @MethodName: getDatasetDatasource
     * @Description: 获取数据集对应的数据源
     */
    Object getDatasetDatasource(ReportDatasource reportDatasource) throws Exception;

    /**
     *<p>Title: getTplGroupDatasets</p>
     *<p>Description: 获取报表模板关联的数据集(分组)</p>
     */
    List<ReportTplDatasetGroup> getTplGroupDatasets(ReportTplDataset dataset, UserInfoDto userInfoDto) throws SQLException, Exception;
}
