package org.vivi.framework.report.service.handler;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.model.reporttpl.ReportTpl;
import org.vivi.framework.report.service.web.dto.UserInfoDto;
import org.vivi.framework.report.service.web.dto.reporttpl.MesGenerateReportDto;
import org.vivi.framework.report.service.web.dto.reporttpl.ReportDataDto;
import org.vivi.framework.report.service.web.dto.reporttpl.ResPreviewData;

public interface IReportTplFormsService {

    /**
     * @MethodName: previewLuckysheetReportFormsData
     * @Description: 填报报表预览数据
     */
    ResPreviewData previewLuckysheetReportFormsData(MesGenerateReportDto mesGenerateReportDto, UserInfoDto userInfoDto, ReportTpl reportTpl, boolean isPagination) throws Exception;

    /**
     * @MethodName: reportData
     * @Description: 上报数据
     */
    BaseEntity reportData(ReportDataDto model, UserInfoDto userInfoDto);

    /**
     * @MethodName: deleteReportData
     * @Description: 填报报表删除数据
     */
    BaseEntity deleteReportData(JSONObject model, UserInfoDto userInfoDto);
}
