package org.vivi.framework.report.service.core.process;


import org.vivi.framework.report.service.model.luckysheetreportcell.LuckysheetReportCell;
import org.vivi.framework.report.service.model.luckysheetreportformscell.LuckysheetReportFormsCell;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ReportDataProcess
 * @Description: 报表数据处理用抽象类
*/
public abstract class LuckySheetBasicDynamicDataProcess {

	public abstract List<LuckySheetBindData> process(List<LuckysheetReportCell> variableCells, List<Map<String, Object>> data, String datasetName,
													 Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells, Map<String, LuckySheetBindData> blockBindDatas,
													 Map<String, Object> subtotalCellDatas, Map<String, Object> subtotalCellMap, String sheetIndex, Map<String, LuckySheetBindData> cellBindData, Map<String, Integer> subTotalDigits, int tplType);
	
	public abstract List<LuckySheetFormsBindData> processForms(List<LuckysheetReportFormsCell> variableCells, List<Map<String, Object>> data, String datasetName, Map<String, Map<String, List<List<Map<String, Object>>>>> processedCells, String sheetIndex);
}
