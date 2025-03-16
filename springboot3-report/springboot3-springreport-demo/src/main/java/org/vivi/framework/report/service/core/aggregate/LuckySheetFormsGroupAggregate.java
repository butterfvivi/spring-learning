package org.vivi.framework.report.service.core.aggregate;


import com.alibaba.fastjson.JSONArray;
import org.vivi.framework.report.service.common.enums.AggregateTypeEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.LuckysheetUtil;
import org.vivi.framework.report.service.common.utils.StringUtil;
import org.vivi.framework.report.service.model.luckysheetreportformscell.LuckysheetReportFormsCell;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.util.*;
import java.util.Map.Entry;

/**  
 * @ClassName: GroupAggregate
 * @Description: 分组聚合处理
*/
public class LuckySheetFormsGroupAggregate extends Aggregate<LuckysheetReportFormsCell, LuckySheetFormsBindData,Map<String, LuckySheetFormsBindData>>{

	@Override
	public LuckySheetFormsBindData aggregate(LuckysheetReportFormsCell reportCell,LuckySheetFormsBindData bindData,Map<String, LuckySheetFormsBindData> cellBinddata) {
		String property = reportCell.getCellValue();
		String[] datasetNames = LuckysheetUtil.getDatasetNames(reportCell.getDatasetName());
		if(datasetNames.length > 1)
		{
			property = property.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}else {
			property = property.replaceAll(datasetNames[0]+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		}
		bindData.setProperty(property);
		List<List<Map<String, Object>>> datas = new ArrayList<List<Map<String,Object>>>();
		List<List<Map<String, Object>>> filterDatas = new ArrayList<List<Map<String,Object>>>();
		JSONArray filters = JSONArray.parseArray(bindData.getCellConditions());
		String groupProperty = property;
		if(StringUtil.isNotEmpty(bindData.getGroupProperty()))
		{
			groupProperty = bindData.getGroupProperty();
		}
		List<List<Map<String, Object>>> bindDatas = null;
		if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
			bindDatas = bindData.getFilterDatas();
		}else {
			bindDatas = bindData.getDatas();
		} 
		if(!ListUtil.isEmpty(bindDatas))
		{
			if(AggregateTypeEnum.GROUP.getCode().equals(bindData.getLastAggregateType()) || AggregateTypeEnum.GROUPSUMMARY.getCode().equals(bindData.getLastAggregateType()))
			{//上次数据处理是分组聚合
				//本次数据处理是分组聚合，则数据进行分组处理
				for (int i = 0; i < bindDatas.size(); i++) {
					Map<String, List<Map<String, Object>>> dataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
					Map<String, List<Map<String, Object>>> filterDataMap = new LinkedHashMap<String, List<Map<String, Object>>>();
					for (int j = 0; j < bindDatas.get(i).size(); j++) {
						Map<String, Object> map = bindDatas.get(i).get(j);
						String value = String.valueOf(map.get(groupProperty));
						List<Map<String, Object>> rowList=null;
						if (dataMap.containsKey(value)) {
							rowList = dataMap.get(value);
						}else {
							rowList = new ArrayList<Map<String,Object>>();
							dataMap.put(value, rowList);
						}
						rowList.add(map);
						List<Map<String, Object>> filterRowList=null;
						if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
						{
							boolean filterResult = ListUtil.filterDatas(filters, map,bindData.getCellConditionType());
							if(filterResult)
							{
								if(filterDataMap.containsKey(value))
								{
									filterRowList = filterDataMap.get(value);
								}else {
									filterRowList = new ArrayList<Map<String,Object>>();
									filterDataMap.put(value, filterRowList);
								}
								filterRowList.add(map);
							}
						}
					}
					Iterator<Entry<String, List<Map<String, Object>>>> entries = dataMap.entrySet().iterator();
					while(entries.hasNext()){
						datas.add(entries.next().getValue());
					}
					Iterator<Entry<String, List<Map<String, Object>>>> entries2 = filterDataMap.entrySet().iterator();
					while(entries2.hasNext()){
						filterDatas.add(entries2.next().getValue());
					}
				}
				bindData.setDatas(datas);
				if(bindData.getIsConditions().intValue() == YesNoEnum.NO.getCode().intValue()) {
					filterDatas = datas;
					bindData.setFilterDatas(datas);
				}
				if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
					bindData.setIsConditions(YesNoEnum.YES.getCode());
				}
				if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
				{
					bindData.setFilterDatas(filterDatas);
				}
				bindData.setLastAggregateType(AggregateTypeEnum.GROUP.getCode());
			}else if(AggregateTypeEnum.LIST.getCode().equals(bindData.getLastAggregateType())){
				//上次数据处理是列表聚合，则不进行分组，直接返回全部数据
				datas.addAll(bindDatas);
				if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
				{
					List<Map<String, Object>> filterCellData = null;
					for (int i = 0; i < bindDatas.size(); i++) {
						filterCellData = new ArrayList<Map<String,Object>>();
						boolean filterResult = ListUtil.filterDatas(filters, bindDatas.get(i).get(0),bindData.getCellConditionType());
						if(filterResult)
						{
							filterCellData.add(bindDatas.get(i).get(0));
							filterDatas.add(filterCellData);
						}
					}
					bindData.setFilterDatas(filterDatas);
				}else {
					bindData.setFilterDatas(datas);
				}
				bindData.setDatas(datas);
				if(bindData.getLastIsConditions().intValue() == YesNoEnum.YES.getCode().intValue()) {
					bindData.setIsConditions(YesNoEnum.YES.getCode());
				}
				bindData.setLastAggregateType(AggregateTypeEnum.LIST.getCode());
			}
		}else {
			bindData.setDatas(datas);
			bindData.setFilterDatas(datas);
			bindData.setLastAggregateType(AggregateTypeEnum.GROUP.getCode());
		}
		return bindData;
	}

}
