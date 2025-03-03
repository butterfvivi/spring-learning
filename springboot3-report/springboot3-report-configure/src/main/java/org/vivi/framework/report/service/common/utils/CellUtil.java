package org.vivi.framework.report.service.common.utils;

import com.alibaba.fastjson.JSONArray;
import org.vivi.framework.report.service.common.enums.CellExtendEnum;
import org.vivi.framework.report.service.common.enums.CellValueTypeEnum;
import org.vivi.framework.report.service.common.enums.ConditionTypeEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.util.*;

/**  
 * @ClassName: CellUtil
 * @Description: 单元格工具类
*/
public class CellUtil {

	
	public static Map<String, LuckySheetBindData> luckySheetBindDataCoordinateMap(List<LuckySheetBindData> datas)
	{
		Map<String, LuckySheetBindData> result = new HashMap<String, LuckySheetBindData>();
		if(!ListUtil.isEmpty(datas))
		{
			for (int i = 0; i < datas.size(); i++) {
				result.put(String.valueOf(datas.get(i).getCoordsx()) + "-" + String.valueOf(datas.get(i).getCoordsy()), datas.get(i));
			}
		}
		return result;
	}
	
	public static Map<String, LuckySheetFormsBindData> luckySheetFormsBindDataCoordinateMap(List<LuckySheetFormsBindData> datas)
	{
		Map<String, LuckySheetFormsBindData> result = new HashMap<String, LuckySheetFormsBindData>();
		if(!ListUtil.isEmpty(datas))
		{
			for (int i = 0; i < datas.size(); i++) {
				result.put(String.valueOf(datas.get(i).getCoordsx()) + "-" + String.valueOf(datas.get(i).getCoordsy()), datas.get(i));
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: processCellFilters
	 * @Description: 处理过滤条件是单元格的，处理单元格数据
	 */
	public static void processCellFilters(JSONArray filters,LuckySheetBindData bindData,Map<String, LuckySheetBindData> cellBindData,String sheetIndex) {
		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(ListUtil.isNotEmpty(filters))
			{
				for (int i = 0; i < filters.size(); i++) {
					String type = filters.getJSONObject(i).getString("type");
					String condition = filters.getJSONObject(i).getString("value");
					if(ConditionTypeEnum.CELL.getCode().equals(type)) {
						if(StringUtil.isNotEmpty(condition)) {
							try {
								int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(condition)) - 1;
								int r = SheetUtil.getRowNum(condition) - 1;
								LuckySheetBindData luckySheetBindData = cellBindData.get(sheetIndex+"-"+r+"-"+c);
								if(luckySheetBindData != null)
								{
									if(CellValueTypeEnum.FIXED.getCode().intValue() == luckySheetBindData.getCellValueType()) {
										//固定值
										Object value = luckySheetBindData.getCellValue();
										filters.getJSONObject(i).put("value", value);
									}else {
										//动态值
										List<List<Map<String, Object>>> datas = null;
										if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
										{
											datas = luckySheetBindData.getFilterDatas();
										}else {
											datas = luckySheetBindData.getDatas();
										}
										if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()) {
											//不扩展
											if(ListUtil.isNotEmpty(datas))
											{
												Object value = datas.get(0).get(0).get(luckySheetBindData.getProperty());
												if(value == null)
												{
													value = "";
												}
												filters.getJSONObject(i).put("value", value);
											}
										}else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()
												|| CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
										{
											Set values = new HashSet<>();
											if(ListUtil.isNotEmpty(datas))
											{
												for (int j = 0; j < datas.size(); j++) {
													for (int j2 = 0; j2 < datas.get(j).size(); j2++) {
														Object value = datas.get(j).get(j2).get(luckySheetBindData.getProperty());
														if(value != null)
														{
															values.add(String.valueOf(value));
														}
													}
												}
											}
											if(ListUtil.isNotEmpty(values))
											{
												String str = String.join(",", values);
												filters.getJSONObject(i).put("value", str);
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public static void processCellFilters(JSONArray filters,LuckySheetFormsBindData bindData,Map<String, LuckySheetFormsBindData> cellBindData,String sheetIndex) {
		if(bindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
		{
			if(ListUtil.isNotEmpty(filters))
			{
				for (int i = 0; i < filters.size(); i++) {
					String type = filters.getJSONObject(i).getString("type");
					String condition = filters.getJSONObject(i).getString("value");
					if(ConditionTypeEnum.CELL.getCode().equals(type)) {
						if(StringUtil.isNotEmpty(condition)) {
							try {
								int c = SheetUtil.excelColStrToNum(SheetUtil.getColumnFlag(condition)) - 1;
								int r = SheetUtil.getRowNum(condition) - 1;
								LuckySheetFormsBindData luckySheetBindData = cellBindData.get(sheetIndex+"-"+r+"-"+c);
								if(luckySheetBindData != null)
								{
									if(CellValueTypeEnum.FIXED.getCode().intValue() == luckySheetBindData.getCellValueType()) {
										//固定值
										Object value = luckySheetBindData.getCellValue();
										filters.getJSONObject(i).put("value", value);
									}else {
										//动态值
										List<List<Map<String, Object>>> datas = null;
										if(luckySheetBindData.getIsConditions().intValue() == YesNoEnum.YES.getCode().intValue())
										{
											datas = luckySheetBindData.getFilterDatas();
										}else {
											datas = luckySheetBindData.getDatas();
										}
										if(CellExtendEnum.NOEXTEND.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()) {
											//不扩展
											if(ListUtil.isNotEmpty(datas))
											{
												Object value = datas.get(0).get(0).get(luckySheetBindData.getProperty());
												if(value == null)
												{
													value = "";
												}
												filters.getJSONObject(i).put("value", value);
											}
										}else if(CellExtendEnum.VERTICAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue()
												|| CellExtendEnum.HORIZONTAL.getCode().intValue() == luckySheetBindData.getCellExtend().intValue())
										{
											Set values = new HashSet<>();
											if(ListUtil.isNotEmpty(datas))
											{
												for (int j = 0; j < datas.size(); j++) {
													for (int j2 = 0; j2 < datas.get(j).size(); j2++) {
														Object value = datas.get(j).get(j2).get(luckySheetBindData.getProperty());
														if(value != null)
														{
															values.add(String.valueOf(value));
														}
													}
												}
											}
											if(ListUtil.isNotEmpty(values))
											{
												String str = String.join(",", values);
												filters.getJSONObject(i).put("value", str);
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
