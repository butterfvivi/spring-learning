package org.vivi.framework.report.service.core.aggregate;

import com.alibaba.fastjson.JSONArray;
import org.vivi.framework.report.service.common.enums.AggregateTypeEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.common.utils.CellUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.LuckysheetUtil;
import org.vivi.framework.report.service.common.utils.StringUtil;
import org.vivi.framework.report.service.model.luckysheetreportcell.LuckysheetReportCell;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ListAggregate
 * @Description: 列表聚合处理
*/
public class LuckySheetListAggregate extends Aggregate<LuckysheetReportCell,LuckySheetBindData,Map<String, LuckySheetBindData>>{

	@Override
	public LuckySheetBindData aggregate(LuckysheetReportCell reportCell,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBinddata) {
		String property = reportCell.getCellValue();
		if(StringUtil.isNotEmpty(property))
		{
			String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
			if(datasetNames.length > 1)
			{
				property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
			}else {
				String cellText =  property.replaceAll(datasetNames[0]+".", "");
				bindData.setCellText(cellText);
				property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
			}
		}
		bindData.setProperty(property);
		bindData.setDatasetName(reportCell.getDatasetName());
		List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String, Object>>> filterDatas = new ArrayList<List<Map<String,Object>>>();
		JSONArray filters = JSONArray.parseArray(bindData.getCellConditions());
		CellUtil.processCellFilters(filters, bindData, cellBinddata, bindData.getSheetIndex());
		List<Map<String, Object>> cellData = null;
		List<Map<String, Object>> filterCellData = null;
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		}
		if(!ListUtil.isEmpty(bindDatas)) {
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
