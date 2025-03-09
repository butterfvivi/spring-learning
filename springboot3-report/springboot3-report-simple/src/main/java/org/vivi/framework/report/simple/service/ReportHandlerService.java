package org.vivi.framework.report.simple.service;

public interface ReportHandlerService {

    void handleExportExcel(String dataJson);

    void generateSheetReportData();
}
