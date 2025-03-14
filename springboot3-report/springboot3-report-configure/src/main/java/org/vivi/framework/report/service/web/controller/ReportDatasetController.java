package org.vivi.framework.report.service.web.controller;

import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.report.service.common.annotation.MethodLog;
import org.vivi.framework.report.service.common.constants.Constants;
import org.vivi.framework.report.service.common.entity.Response;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.handler.IReportTplDatasetService;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;
import org.vivi.framework.report.service.model.reporttpldatasetgroup.ReportTplDatasetGroup;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.ApiTestResultDto;
import org.vivi.framework.report.service.web.dto.reporttpldataset.MesGetRelyOnSelectData;
import org.vivi.framework.report.service.web.dto.reporttpldataset.ReportDatasetDto;
import org.vivi.framework.report.service.web.dto.reporttpldataset.ReportTplDatasetDto;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportTplDataset")
public class ReportDatasetController {

    @Autowired
    private IReportTplDatasetService iReportTplDatasetService;


    /**
     * @Description: 单条删除
     * @param id
     * @return  BaseEntity
     * @throws
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    @MethodLog(module="ReportTplDataset",remark="单条删除",operateType= Constants.OPERATE_TYPE_DELETE)
    public Response delete(@RequestParam Long id)
    {
        BaseEntity result = iReportTplDatasetService.delete(id);
        return Response.success(result.getStatusMsg());
    }

    /**
     * @Description: 获取模板关联的数据集
     */
    @RequestMapping(value = "/getTplDatasets",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取模板关联的数据集",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getTplDatasets(@RequestBody ReportTplDataset dataset, UserInfoDto userInfoDto) throws Exception {
        List<ReportDatasetDto> result = this.iReportTplDatasetService.getTplDatasets(dataset,userInfoDto);
        return Response.success(result);
    }

    /**
     * @MethodName: getDatasetColumns
     * @Description: 获取数据集字段
     */
    @RequestMapping(value = "/getDatasetColumns",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取数据集字段",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getDatasetColumns(@RequestBody ReportTplDataset dataset, UserInfoDto userInfoDto) throws Exception
    {
        List<Map<String, Object>> result = this.iReportTplDatasetService.getDatasetColumns(dataset,userInfoDto);
        return Response.success(result);
    }

    /**
     * @MethodName: getApiDefaultRequestResult
     * @Description: 获取api数据集默认参数请求结果
     */
    @RequestMapping(value = "/getApiDefaultRequestResult",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取api数据集默认参数请求结果",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getApiDefaultRequestResult(@RequestBody ReportTplDataset dataset) {
        ApiTestResultDto result = this.iReportTplDatasetService.getApiDefaultRequestResult(dataset);
        return Response.success(result);
    }

    /**
     *<p>Title: addTplDataSets</p>
     *<p>Description: 报表模板添加数据集</p>
     */
    @RequestMapping(value = "/addTplDataSets",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="添加模板数据集",operateType=Constants.OPERATE_TYPE_ADD)
    public Response addTplDataSets(@RequestBody ReportTplDataset reportTplDataset, UserInfoDto userInfoDto) throws Exception {
        ReportDatasetDto result = this.iReportTplDatasetService.addTplDataSets(reportTplDataset,userInfoDto);
        return Response.success(result);
    }

    /**
     * @Title: getReportDatasetsParam
     * @Description: 预览报表获取报表数据集参数
     */
    @RequestMapping(value = "/getReportDatasetsParam",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="预览获取报表数据集参数",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getReportDatasetsParam(@RequestBody ReportTplDatasetDto reportTplDatasetDto, UserInfoDto userInfoDto) throws ParseException
    {
        Map<String, Object> result = this.iReportTplDatasetService.getReportDatasetsParam(reportTplDatasetDto);
        return Response.success(result);
    }

    /**
     * @MethodName: getShareReportDatasetsParam
     * @Description: 预览报表获取报表数据集参数(分享链接用)
     */
//    @RequestMapping(value = "/getShareReportDatasetsParam",method = RequestMethod.POST)
//    public Response getShareReportDatasetsParam(@RequestBody ReportTplDatasetDto reportTplDataset) throws ParseException
//    {
//        String shareCode = this.httpServletRequest.getHeader("shareCode");
//        String shareUser = this.httpServletRequest.getHeader("shareUser");
//        if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
//        {
//            throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
//        }
//        Map<String, Object> result = this.iReportTplDatasetService.getReportDatasetsParam(reportTplDataset);
//        return Response.success(result);
//    }


    /**
     * @Title: getTplDatasetColumns
     * @Description: 获取数据集的列
     * @param reportTplDataset
     */
    @RequestMapping(value = "/getTplDatasetColumns",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取数据集的列",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getTplDatasetColumns(@RequestBody ReportTplDataset reportTplDataset, UserInfoDto userInfoDto) throws Exception
    {
        List<Map<String, Object>> result = this.iReportTplDatasetService.getTplDatasetColumns(reportTplDataset,userInfoDto);
        return Response.success(result);
    }


    /**
     * @MethodName: getSelectData
     * @Description: 获取下拉数据
     * @author caiyang
     * @param mesGetRelyOnSelectData
     */
    @RequestMapping(value = "/getSelectData",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
        List<Map<String, Object>> result = this.iReportTplDatasetService.getSelectData(mesGetRelyOnSelectData);
        return Response.success(result);
    }

    /**
     * @MethodName: getRelyOnSelectData
     * @Description: 获取依赖项的下拉数据
     * @author caiyang
     * @param mesGetRelyOnSelectData
     */
    @RequestMapping(value = "/getRelyOnData",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取依赖项的下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getRelyOnSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
        List<Map<String, Object>> result = this.iReportTplDatasetService.getRelyOnSelectData(mesGetRelyOnSelectData);
        return Response.success(result);
    }

    /**
     * @MethodName: getTreeSelectData
     * @Description: 获取下拉树数据
     * @author caiyang
     * @param mesGetRelyOnSelectData
     */
    @RequestMapping(value = "/getTreeSelectData",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取下拉数据",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getTreeSelectData(@RequestBody MesGetRelyOnSelectData mesGetRelyOnSelectData) throws JSQLParserException {
        List<Map<String, Object>> result = this.iReportTplDatasetService.getTreeSelectData(mesGetRelyOnSelectData);
        return Response.success(result);
    }

    /**
     * @Description: 获取模板关联的数据集（分组）
     */
    @RequestMapping(value = "/getTplGroupDatasets",method = RequestMethod.POST)
    @MethodLog(module="ReportTplDataset",remark="获取模板关联的数据集(分组)",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getTplGroupDatasets(@RequestBody ReportTplDataset dataset, UserInfoDto userInfoDto) throws Exception {
        List<ReportTplDatasetGroup> result = this.iReportTplDatasetService.getTplGroupDatasets(dataset,userInfoDto);
        return Response.success(result);
    }
}
