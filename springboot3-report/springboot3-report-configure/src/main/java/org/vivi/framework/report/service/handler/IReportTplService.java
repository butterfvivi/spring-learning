package org.vivi.framework.report.service.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.entity.PageEntity;
import org.vivi.framework.report.service.model.reporttpl.ReportTpl;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reporttpl.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface IReportTplService extends IService<ReportTpl> {

   /**
    * @Title: tablePagingQuery
    * @Description: 表格分页查询
    */
   PageEntity tablePagingQuery(ReportTpl model);

   List<ReportTplTreeDto> getChildren(ReportTpl model);

   /**
    *<p>Title: getDetail</p>
    *<p>Description: 获取详情</p>
    */
   BaseEntity getDetail(Long id);

   /**
    *<p>Title: insert</p>
    *<p>Description: 新增数据</p>
    */
   BaseEntity insert(ReportTplDto model);

   /**
    *<p>Title: update</p>
    *<p>Description: 更新数据</p>
    */
   BaseEntity update(ReportTplDto model);

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
    * @Title: getReports
    * @Description: 获取所有的报表
    */
   List<ReportTpl> getReports();

   /**
    * @Title: saveLuckySheetTpl
    * @Description: 保存luckysheet报表模板
    */
   BaseEntity saveLuckySheetTpl(MesLuckysheetsTplDto mesLuckySheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException;

   /**
    * @Title: getLuckySheetTplSettings
    * @Description: 获取luckysheet模板内容
    */
   ResSheetsSettingsDto getLuckySheetTplSettings(ReportTpl reportTpl, UserInfoDto userInfoDto) throws Exception;

   /**
    * @Title: previewLuckysheetReportData
    * @Description: luckysheet预览报表
    */
   ResPreviewData previewLuckysheetReportData(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto) throws Exception;

   /**
    * @Title: luckySheetExportExcel
    * @Description: luckysheet导出excel
    */
   void luckySheetExportExcel(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto, HttpServletResponse httpServletResponse) throws Exception;


   /**
    * @MethodName: saveReportFormsTpl
    * @Description: 保存填报模板
    */
   BaseEntity saveReportFormsTpl(MesLuckysheetsTplDto mesLuckysheetsTplDto,UserInfoDto userInfoDto) throws JsonProcessingException, SQLException;

   /**
    * @MethodName: getReportFormsTpl
    * @Description: 获取填报模板
    */
   ResSheetsSettingsDto getReportFormsTpl(ReportTpl reportTpl,UserInfoDto userInfoDto) throws JsonMappingException, JsonProcessingException;

   /**
    * @MethodName: copyReport
    * @Description: 复制报表
    */
   BaseEntity copyReport(ReportTplDto model);

   /**
    * @MethodName: transf2OnlineReport
    * @Description: 转成在线报表文档
    */
   BaseEntity transf2OnlineReport(MesGenerateReportDto mesGenerateReportDto,UserInfoDto userInfoDto) throws Exception;


   JSONArray uploadExcel(MultipartFile file, String gridKey, UserInfoDto userInfoDto) throws Exception;

   /**
    * @MethodName: uploadReportTpl
    * @Description: 上传报表模板(excel)
    */
   JSONArray uploadReportTpl(MultipartFile file,long tplId,int isFormsReport,UserInfoDto userInfoDto) throws Exception;

   /**
    * @Title: getTplDatasetsColumnNames
    * @Description: 获取模板数据集所有的列名，列名格式是dataSetName.${columnName}，后期可以优化，放到redis中
    */
   List<List<String>> getTplDatasetsColumnNames(Long tplId,Map<String, String> datasetNameIdMap,UserInfoDto userInfoDto) throws SQLException;

   /**
    * @MethodName: getAllTpls
    * @Description: 获取所有的模板
    */
   List<ReportTpl> getAllTpls(ReportTpl model);

   /**
    * @MethodName: getTplAuth
    * @Description: 获取模板权限
    */
   JSONObject getTplAuth(ReportTpl model, UserInfoDto userInfoDto);

}
