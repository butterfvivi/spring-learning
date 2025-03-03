package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.IReportSheetPdfPrintSettingService;
import org.vivi.framework.report.service.mapper.reportsheetpdfprintsetting.ReportSheetPdfPrintSettingMapper;
import org.vivi.framework.report.service.model.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

@Service
public class ReportSheetPdfPrintSettingServiceImpl extends ServiceImpl<ReportSheetPdfPrintSettingMapper, ReportSheetPdfPrintSetting> implements IReportSheetPdfPrintSettingService {
}
