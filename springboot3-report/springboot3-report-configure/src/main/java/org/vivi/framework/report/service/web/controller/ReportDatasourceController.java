/** 
 * 模块：报表系统-ReportDatasource
 */
package org.vivi.framework.report.service.web.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.entity.Response;
import org.vivi.framework.report.service.handler.IReportDatasourceService;
import org.vivi.framework.report.service.model.reportdatasource.ReportDatasource;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.MesGetSelectDataDto;
import org.vivi.framework.report.service.web.dto.reportdatasource.MesReportDatasourceDto;
import org.vivi.framework.report.service.web.dto.reporttpldatasource.MesExecSqlDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportDatasource")
public class ReportDatasourceController {

   /**
    * iReportDatasourceService服务注入
    */
   @Autowired
   private IReportDatasourceService iReportDatasourceService;

   /**
   * @Description: 获取页面表格数据
   * @throws
   */
   @RequestMapping(value = "/getTableList",method = RequestMethod.POST)
   public Response getTableList(@RequestBody ReportDatasource model)
   {
       BaseEntity result = new BaseEntity();
       result = iReportDatasourceService.tablePagingQuery(model);
       return Response.success(result);
   }

   /**
   * @Description: 获取详细信息
   */
   @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
   public Response getDetail(@RequestParam Long id) throws Exception
   {
       BaseEntity result = iReportDatasourceService.getDetail(id);
       return Response.success(result);
   }

   /**
   * @throws Exception
   * @Description: 新增
   */
   @RequestMapping(value = "/insert",method = RequestMethod.POST)
   //@Check({"code:required#编码;length#编码#50","name:required#数据库名称;length#数据库名称#40","jdbcUrl:required#数据库链接url;length#数据库链接url#500"})
   public Response insert(@RequestBody ReportDatasource model) throws Exception
   {
       BaseEntity result = iReportDatasourceService.insert(model);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @throws Exception
   * @Description: 更新
   */
   @RequestMapping(value = "/update",method = RequestMethod.POST)
   //@Check({"id:required#主键ID","code:required#编码;length#编码#50","name:required#数据库名称;length#数据库名称#40","jdbcUrl:required#数据库链接url;length#数据库链接url#500"})
   public Response update(@RequestBody ReportDatasource model) throws Exception
   {
       BaseEntity result = iReportDatasourceService.update(model);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @Description: 单条删除
   */
   @RequestMapping(value = "/delete",method = RequestMethod.GET)
   //@Check({"id:required#主键ID"})
   public Response delete(@RequestParam Long id)
   {
       BaseEntity result = iReportDatasourceService.delete(id);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @Description: 批量删除
   */
   @RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
   public Response deletebatch(@RequestBody List<Long> ids)
   {
       BaseEntity result = iReportDatasourceService.deleteBatch(ids);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @Description: 获取数据源
   * @return Response
   * @throws
   */
   @RequestMapping(value = "/getReportDatasource",method = RequestMethod.POST)
   public Response getReportDatasource(@RequestBody MesReportDatasourceDto mesReportDatasourceDto) {
       List<ReportDatasource> result = this.iReportDatasourceService.getReportDatasource(mesReportDatasourceDto);
       return Response.success(result);
   }

   /**
   * @Description: execSql
   *<p>Description: 执行解析sql语句</p>
   */
   @RequestMapping(value = "/execSql",method = RequestMethod.POST)
   //@Check({"tplSql:required#sql语句","datasourceId:required#数据库id"})
   public Response execSql(@RequestBody MesExecSqlDto mesExecSqlDto,  UserInfoDto userInfoDto) throws Exception {
       List<Map<String, Object>> result = this.iReportDatasourceService.execSql(mesExecSqlDto,userInfoDto);
       return Response.success(result);
   }

   /**
    * @Title: connectionTest
    * @Description: 数据源连接测试
    * @param reportDatasource
    */
   @RequestMapping(value = "/connectionTest",method = RequestMethod.POST)
   //@Check({"type:required#数据源类型","jdbcUrl:required#数据源连接"})
   public Response connectionTest(@RequestBody ReportDatasource reportDatasource) {
       BaseEntity result = this.iReportDatasourceService.connectionTest(reportDatasource);
       return Response.success(result,result.getStatusMsg());
   }

   /**
    * @Title: getDatasourceSelectData
    * @Description: 获取下拉框数据
    */
   @RequestMapping(value = "/getDatasourceSelectData",method = RequestMethod.POST)
   //@Check({"dataSourceId:required#数据源id"})
   public Response getDatasourceSelectData(@RequestBody MesGetSelectDataDto mesGetSelectDataDto) {
       List<Map<String, Object>> result = this.iReportDatasourceService.getDatasourceSelectData(mesGetSelectDataDto);
       return Response.success(result);
   }

   /**
    * @MethodName: getDatabseTables
    * @Description: 获取数据库的所有表信息
    */
   @RequestMapping(value = "/getDatabseTables",method = RequestMethod.POST)
   //@Check({"id:required#数据源id"})
   public Response getDatabseTables(@RequestBody ReportDatasource datasource)
   {
       List<Map<String, String>> result = this.iReportDatasourceService.getDatabseTables(datasource);
       return Response.success(result);
   }

   /**
    * @MethodName: parseApiResultAttr
    * @Description: 解析api数据集结果属性
    */
   @RequestMapping(value = "/parseApiResultAttr",method = RequestMethod.POST)
   //@Check({"apiResultType:required#返回值类型","apiRequestType:required#请求方式","jdbcUrl:required#请求链接"})
   public Response parseApiResultAttr(@RequestBody ReportDatasource model) {
       JSONArray result = this.iReportDatasourceService.parseApiResultAttr(model);
       return Response.success(result);
   }
}
