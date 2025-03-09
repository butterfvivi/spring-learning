/** 
 * 模块：报表系统-ReportTpl
 */
package org.vivi.framework.report.service.web.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.service.common.annotation.MethodLog;
import org.vivi.framework.report.service.common.constants.Constants;
import org.vivi.framework.report.service.common.entity.Response;
import org.vivi.framework.report.service.common.constants.StatusCode;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.enums.DelFlagEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.common.exception.BizException;
import org.vivi.framework.report.service.common.utils.GzipUtils;
import org.vivi.framework.report.service.common.utils.MessageUtil;
import org.vivi.framework.report.service.common.utils.StringUtil;
import org.vivi.framework.report.service.model.reporttpl.ReportTpl;
import org.vivi.framework.report.service.handler.IReportTplFormsService;
import org.vivi.framework.report.service.handler.IReportTplService;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reporttpl.*;
import org.vivi.framework.report.service.common.entity.PageEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportTpl")
public class ReportTplController   {

   /**
    * iReportTplService服务注入
    */
   @Autowired
   private IReportTplService iReportTplService;

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    /**
     * @Description: 表格分页查询
     * @param model
     * @return
     */

   @RequestMapping(value = "/getTableList",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取页面表格数据",operateType= Constants.OPERATE_TYPE_SEARCH)
   public Response getTableList(@RequestBody ReportTpl model)
   {
       PageEntity result = iReportTplService.tablePagingQuery(model);
       return Response.success(result);
   }

   @RequestMapping(value = "/getChildren",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取页面表格数据",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getChildren(@RequestBody ReportTpl model)
   {
       List<ReportTplTreeDto> result = iReportTplService.getChildren(model);
       return Response.success(result);
   }

   /**
   * @Description: 获取详细信息
   */
   @RequestMapping(value = "/getDetail",method = RequestMethod.GET)
   @MethodLog(module="ReportTpl",remark="获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
    public Response getDetail(@RequestParam Long id) throws Exception
   {
       BaseEntity result = iReportTplService.getDetail(id);
       return Response.success(result);
   }

   /**
    * @MethodName: getShareDetail
    * @Description: 分享获取详情
    */
   @RequestMapping(value = "/getShareDetail",method = RequestMethod.GET)
   @MethodLog(module="ReportTpl",remark="分享获取详细信息",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getShareDetail(@RequestParam Long id) throws Exception
   {
       BaseEntity result = iReportTplService.getDetail(id);
       return Response.success(result);
   }

   /**
   * @throws Exception
   * @Description: 新增
   */
   @RequestMapping(value = "/insert",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="新增",operateType=Constants.OPERATE_TYPE_ADD)
   public Response insert(@RequestBody ReportTplDto model) throws Exception
   {
       BaseEntity result = iReportTplService.insert(model);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @throws Exception
   * @Description: 更新
   */
   @RequestMapping(value = "/update",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="更新",operateType=Constants.OPERATE_TYPE_UPDATE)
   public Response update(@RequestBody ReportTplDto model) throws Exception
   {
       BaseEntity result = iReportTplService.update(model);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @Description: 单条删除
   * @param id
   */
   @RequestMapping(value = "/delete",method = RequestMethod.GET)
   @MethodLog(module="ReportTpl",remark="单条删除",operateType=Constants.OPERATE_TYPE_DELETE)
   public Response delete(@RequestParam Long id)
   {
       BaseEntity result = iReportTplService.delete(id);
       return Response.success(result.getStatusMsg());
   }

   /**
   * @Description: 批量删除
   */
   @RequestMapping(value = "/deletebatch",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="批量删除",operateType=Constants.OPERATE_TYPE_BATCHDELETE)
   public Response deletebatch(@RequestBody List<Long> ids)
   {
       BaseEntity result = iReportTplService.deleteBatch(ids);
       return Response.success(result.getStatusMsg());
   }


   /**
   * @Description: 获取所有的报表
   */
   @RequestMapping(value = "/getReports",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取所有的报表",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getReports()
   {
       List<ReportTpl> result = iReportTplService.getReports();
       return Response.success(result);
   }

   /**
   * saveLuckySheetTpl
   *<p>Description: 保存luckysheet模板</p>
   */
   @RequestMapping(value = "/saveLuckySheetTpl",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="保存模板",operateType=Constants.OPERATE_TYPE_UPDATE)
   public Response saveLuckySheetTpl(@RequestBody MesLuckysheetsTplDto mesLuckysheetsTplDto,UserInfoDto userInfoDto) throws Exception
   {
       BaseEntity result = this.iReportTplService.saveLuckySheetTpl(mesLuckysheetsTplDto,userInfoDto);
       return Response.success(result,result.getStatusMsg());
   }

   /**
    * @Title: getTplSettings
    * @Description: 获取模板设置
    */
   @RequestMapping(value = "/getLuckySheetTplSettings",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取模板设置",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getLuckySheetTplSettings(@RequestBody ReportTpl reportTpl,UserInfoDto userInfoDto) throws Exception {
       ResSheetsSettingsDto result = this.iReportTplService.getLuckySheetTplSettings(reportTpl,userInfoDto);
       return Response.success(result);
   }

   /**
    * @Title: previewLuckysheetReportData
    * @Description: luckysheet预览报表
    */
   @RequestMapping(value = "/previewLuckysheetReportData",method = RequestMethod.POST)
   public String previewLuckysheetReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception
   {
       ResPreviewData result = this.iReportTplService.previewLuckysheetReportData(mesGenerateReportDto,userInfoDto);
       httpServletResponse.setHeader("Content-Encoding", "gzip");
       httpServletResponse.setContentType("text/html");
       String resultStr="";
       Response response = new Response();
       response.setCode(200);
       response.setData(result);
       resultStr=JSONUtil.toJsonStr(response);
       try {
            byte dest[]= GzipUtils.compress2(resultStr);
            OutputStream out=httpServletResponse.getOutputStream();
            out.write(dest);
            out.close();
            out.flush();
        } catch (Exception e) {
           e.printStackTrace();
        }
       return null;
   }

   /**
    * @MethodName: previewShareLuckysheetReportData
    * @Description: luckysheet预览报表(分享链接用)
    */
   @RequestMapping(value = "/previewShareLuckysheetReportData",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="分享预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
   public String previewShareLuckysheetReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception
   {
       String shareCode = this.httpServletRequest.getHeader("shareCode");
       String shareUser = this.httpServletRequest.getHeader("shareUser");
       if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
       {
           throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
       }
       ResPreviewData result = this.iReportTplService.previewLuckysheetReportData(mesGenerateReportDto,null);
       httpServletResponse.setHeader("Content-Encoding", "gzip");
       httpServletResponse.setContentType("text/html");
       String resultStr="";
       Response response = new Response();
       response.setCode(200);
       response.setData(result);
       resultStr=JSONUtil.toJsonStr(response);
       try {
            byte dest[]= GzipUtils.compress2(resultStr);
            OutputStream out=httpServletResponse.getOutputStream();
            out.write(dest);
            out.close();
            out.flush();
        } catch (Exception e) {
           e.printStackTrace();
        }
       return null;
   }

   /**
    * @Title: exportExcel
    * @Description: 导出excel数据
    */
   @RequestMapping(value = "/luckySheetExportExcel",method = RequestMethod.POST)
   public void luckySheetExportExcel(@RequestBody MesGenerateReportDto mesGenerateReportDto,  UserInfoDto userInfoDto) throws Exception {
       this.iReportTplService.luckySheetExportExcel(mesGenerateReportDto,userInfoDto, httpServletResponse);
   }

   /**
    * @MethodName: shareLuckySheetExportExcel
    * @Description: 导出excel数据（分享链接用）
    */
   @RequestMapping(value = "/shareLuckySheetExportExcel",method = RequestMethod.POST)
   public void shareLuckySheetExportExcel(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception {
       String shareCode = this.httpServletRequest.getHeader("shareCode");
       String shareUser = this.httpServletRequest.getHeader("shareUser");
       if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
       {
           throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
       }
       this.iReportTplService.luckySheetExportExcel(mesGenerateReportDto,null, httpServletResponse);
   }


   /**
    * @MethodName: saveReportFormsTpl
    * @Description: 保存填报模板
    */
   @RequestMapping(value = "/saveReportFormsTpl",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="保存填报模板",operateType=Constants.OPERATE_TYPE_UPDATE)
   public Response saveReportFormsTpl(@RequestBody MesLuckysheetsTplDto mesLuckysheetsTplDto,  UserInfoDto userInfoDto) throws Exception
   {
       BaseEntity result = this.iReportTplService.saveReportFormsTpl(mesLuckysheetsTplDto,userInfoDto);
       return Response.success(result,result.getStatusMsg());
   }

   /**
    * @MethodName: getReportFormsTpl
    * @Description: 获取填报报表模板
    */
   @RequestMapping(value = "/getReportFormsTpl",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取填报报表模板",operateType=Constants.OPERATE_TYPE_UPDATE)
   public Response getReportFormsTpl(@RequestBody ReportTpl reportTpl, UserInfoDto userInfoDto) throws Exception
   {
       ResSheetsSettingsDto result = this.iReportTplService.getReportFormsTpl(reportTpl,userInfoDto);
       return Response.success(result);
   }

   /**
    * @Title: previewLuckysheetFormsReportData
    * @Description: luckysheet预览报表
    */
   @RequestMapping(value = "/previewLuckysheetFormsReportData",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="预览luckysheet报表",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response previewLuckysheetFormsReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception
   {
//		ResPreviewData result = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,userInfoDto);
       return Response.success(null);
   }

//   /**
//    * @Title: previewShareLuckysheetFormsReportData
//    * @Description: luckysheet预览报表(分享链接用)
//    */
//   @RequestMapping(value = "/previewShareLuckysheetFormsReportData",method = RequestMethod.POST)
//   public Response previewShareLuckysheetFormsReportData(@RequestBody MesGenerateReportDto mesGenerateReportDto) throws Exception
//   {
//       String shareCode = this.httpServletRequest.getHeader("shareCode");
//       String shareUser = this.httpServletRequest.getHeader("shareUser");
//       if(StringUtil.isNullOrEmpty(shareCode) || StringUtil.isNullOrEmpty(shareUser))
//       {
//           throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.share.param"));
//       }
//       ReportTpl reportTpl = this.iReportTplService.getById(mesGenerateReportDto.getTplId());
//       if (reportTpl == null) {
//           throw new BizException(StatusCode.FAILURE, MessageUtil.getValue("error.check.notexist", new String[] {"报表模板"}));
//       }
//       ResPreviewData result = this.iReportTplFormsService.previewLuckysheetReportFormsData(mesGenerateReportDto,null,reportTpl,true);
//       return Response.success(result);
//   }

   /**
    * @MethodName: copyReport
    * @Description: 复制报表
    */
   @RequestMapping(value = "/copyReport",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="复制报表",operateType=Constants.OPERATE_TYPE_ADD)
   public Response copyReport(@RequestBody ReportTplDto model) {
       BaseEntity result = this.iReportTplService.copyReport(model);
       return Response.success(result.getStatusMsg());
   }

   /**
    * @MethodName: transf2OnlineReport
    * @Description: 转成在线报表文档
    */
   @RequestMapping(value = "/transf2OnlineReport",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="转成在线报表文档",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response transf2OnlineReport(@RequestBody MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception {
       BaseEntity result = this.iReportTplService.transf2OnlineReport(mesGenerateReportDto, userInfoDto);
       return Response.success(result);
   }

   /**
    * @MethodName: uploadExcel
    * @Description: 上传excel文件解析其中的数据
    */
   @RequestMapping("/uploadExcel")
   public Response uploadExcel(@RequestParam("file") MultipartFile file,@RequestParam("gridKey") String gridKey, UserInfoDto userInfoDto) throws Exception  {
       JSONArray result = this.iReportTplService.uploadExcel(file,gridKey,userInfoDto);
       return Response.success(result);
   }


   /**
    * @MethodName: uploadReportTpl
    * @Description: 上传报表模板(excel)
    */
   @RequestMapping("/uploadReportTpl")
   public String uploadReportTpl(@RequestParam("file") MultipartFile file,@RequestParam("tplId") long tplId,@RequestParam("isFormsReport") int isFormsReport, UserInfoDto userInfoDto) throws Exception  {
       httpServletResponse.setHeader("Content-Encoding", "gzip");
       httpServletResponse.setContentType("text/html");
       String resultStr="";
       JSONArray result = this.iReportTplService.uploadReportTpl(file, tplId,isFormsReport, userInfoDto);;
       Response response = new Response();
       response.setCode(200);
       response.setData(result);
       resultStr=JSONUtil.toJsonStr(response);
       try {
            byte dest[]= GzipUtils.compress2(resultStr);
            OutputStream out=httpServletResponse.getOutputStream();
            out.write(dest);
            out.close();
            out.flush();
        } catch (Exception e) {
           e.printStackTrace();
        }
       return null;
   }

   /**
   * @Description: 获取所有的模板
   */
   @RequestMapping(value = "/getAllTpls",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取所有的模板",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getAllTpls(@RequestBody ReportTpl model)
   {
       List<ReportTpl> tpls = iReportTplService.getAllTpls(model);
       return Response.success(tpls);
   }

   /**
    * @MethodName: getTplAuth
    * @Description: 获取模板授权范围
    */
   @RequestMapping(value = "/getTplAuth",method = RequestMethod.POST)
   @MethodLog(module="ReportTpl",remark="获取模板权限",operateType=Constants.OPERATE_TYPE_SEARCH)
   public Response getTplAuth(@RequestBody ReportTpl model, UserInfoDto userInfoDto)
   {
       JSONObject result = this.iReportTplService.getTplAuth(model, userInfoDto);
       return Response.success(result);
   }

}
