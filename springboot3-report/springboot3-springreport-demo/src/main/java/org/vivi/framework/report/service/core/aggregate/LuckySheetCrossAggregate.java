package org.vivi.framework.report.service.core.aggregate;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import org.vivi.framework.report.service.model.luckysheetreportcell.LuckysheetReportCell;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;

import java.util.*;
import java.util.Map.Entry;

/**  
 * @ClassName: LuckySheetCrossAggregate
 * @Description: 交叉数据处理
*/
public class LuckySheetCrossAggregate extends Aggregate<LuckysheetReportCell, LuckySheetBindData,Map<String, LuckySheetBindData>>{

	@Override
	public LuckySheetBindData aggregate(LuckysheetReportCell reportCell,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBinddata) {
//		String property = reportCell.getCellValue().split("\\.")[1].replace("${", "").replace("}", "");//单元格属性值
		String property = reportCell.getCellValue().replaceAll(reportCell.getDatasetName()+".", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("}", "");
		bindData.setProperty(property);
		String groupProperty = bindData.getGroupProperty();
		if(StringUtil.isNotEmpty(groupProperty)) {
			List<String> categories = new ArrayList<>();
			String[] properties = groupProperty.split(",");
			List<List<Map>> bindDatas = new ArrayList<>();
			List<Map> json = JSON.parseArray(JSON.toJSONString(bindData.getOriginalData()), Map.class);
			bindDatas.add(json);
			bindDatas = this.groupDatas(bindDatas, properties);
			for (int i = 0; i < bindDatas.size(); i++) {
				String item = "";
				for (int j = 0; j < properties.length; j++) {
					Object value = bindDatas.get(i).get(0).get(properties[j]);
					if(j == 0) {
						item = item + String.valueOf(value);
					}else {
						item = "_" + item + String.valueOf(value);
					}
				}
				categories.add(item);
			}
			List<List<Map<String, Object>>> datas = new ArrayList<>();
			for (int i = 0; i < bindData.getDatas().size(); i++) {
				Map<String, Map<String, Object>> mapDatas = this.processDatas(bindData.getDatas().get(i), properties);
				List<Map<String, Object>> groupDatas = new ArrayList<>();
				for (int j = 0; j < categories.size(); j++) {
					if(mapDatas.containsKey(categories.get(j))) {
						groupDatas.add(mapDatas.get(categories.get(j)));
					}else {
						groupDatas.add(new HashMap<>());
					}
				}
				datas.add(groupDatas);
			}
			bindData.setDatas(datas);
		}
		return bindData;
	}
	
	 private  List<List<Map>> groupDatas(List<List<Map>> bindDatas,String[] properties){
	    	List<List<Map>> datas = null;
	    	for (int t = 0; t < properties.length; t++) {
	    		datas = new ArrayList<List<Map>>();
	    		for (int i = 0; i < bindDatas.size(); i++) {
	    			Map<String, List<Map>> dataMap = new LinkedHashMap<String, List<Map>>();
	    			for (int j = 0; j < bindDatas.get(i).size(); j++) {
	    				Map<String, Object> map = bindDatas.get(i).get(j);
	    				String value = String.valueOf(map.get(properties[t]));
	    				List<Map> rowList=null;
						if (dataMap.containsKey(value)) {
							rowList = dataMap.get(value);
						}else {
							rowList = new ArrayList<Map>();
							dataMap.put(value, rowList);
						}
						rowList.add(map);
	    			}
	    			Iterator<Entry<String, List<Map>>> entries = dataMap.entrySet().iterator();
					while(entries.hasNext()){
						datas.add(entries.next().getValue());
					}
	    		}
	    		bindDatas = datas;
	    	}
	    	return bindDatas;
	    }
	 
	 private Map<String, Map<String, Object>> processDatas(List<Map<String, Object>> datas,String[] properties){
			Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
			for (int i = 0; i < datas.size(); i++) {
				String item = "";
				for (int t = 0; t < properties.length; t++) {
					Object value = datas.get(i).get(properties[t]);
					if(t == 0) {
						item = item + String.valueOf(value);
					}else {
						item = "_" + item + String.valueOf(value);
					}
				}
				if(!result.containsKey(item)) {
					result.put(item, datas.get(i));
				}
			}
			return result;
		}
}
