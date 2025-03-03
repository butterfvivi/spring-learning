package org.vivi.framework.report.service.core.aggregate;

import com.alibaba.fastjson.JSONArray;
import org.vivi.framework.report.service.common.enums.AggregateTypeEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.common.utils.CellUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.LuckysheetUtil;
import org.vivi.framework.report.service.model.luckysheetreportformscell.LuckysheetReportFormsCell;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ListAggregate
 * @Description: 列表聚合处理
*/
public class LuckySheetFormsListAggregate extends Aggregate<LuckysheetReportFormsCell,LuckySheetFormsBindData,Map<String, LuckySheetFormsBindData>>{

	@Override
	public LuckySheetFormsBindData aggregate(LuckysheetReportFormsCell reportCell, LuckySheetFormsBindData bindData, Map<String, LuckySheetFormsBindData> cellBinddata) {
		String property = reportCell.getCellValue();
		String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
		if(datasetNames.length > 1)
		{
			property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}else {
			property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}
		bindData.setProperty(property);
		bindData.setDatasetName(reportCell.getDatasetName());
		List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String, Object>>> filterDatas = new ArrayList<List<Map<String,Object>>>();
		JSONArray filters = JSONArray.parseArray(bindData.getCellConditions());
		CellUtil.processCellFilters(filters, bindData, cellBinddata, bindData.getSheetIndex());
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		}
		if(!ListUtil.isEmpty(bindDatas))
		{
			List<Map<String, Object>> cellData = null;
			List<Map<String, Object>> filterCellData = null;
			for (int i = 0; i < bindDatas.size(); i++) {
				for (int j = 0; j < bindDatas.get(i).size(); j++) {
					cellData = new ArrayList<Map<String,Object>>();
					filterCellData =  new ArrayList<Map<String,Object>>(); 
					cellData.add(bindDatas.get(i).get(j));
					datas.add(cellData);
					if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
					{
						boolean filterResult = ListUtil.filterDatas(filters, bindDatas.get(i).get(j),bindData.getCellConditionType());
						if(filterResult)
						{
							filterCellData.add(bindDatas.get(i).get(j));
							filterDatas.add(filterCellData);
						}
					}else {
						filterCellData.add(bindDatas.get(i).get(j));
						filterDatas.add(filterCellData);
					}
				}
			}
			if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
				bindData.setIsConditions(YesNoEnum.YES.getCode());
			}
			bindData.setDatas(datas);
			if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
			{
				bindData.setFilterDatas(filterDatas);
			}
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}else {
			bindData.setDatas(datas);
			bindData.setFilterDatas(datas);
			bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
		}
		return bindData;
	}

}
