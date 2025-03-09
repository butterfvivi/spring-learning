package org.vivi.framework.report.simple.utils;

import ch.qos.logback.core.util.StringUtil;
import java.util.*;

public class ListUtil {

    
    /**  
     * @Title: isEmpty
     * @Description: 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection)
    {
    	if(collection == null || collection.size() == 0)
    	{
    		return true;
    	}
    	return false;
    }
    
    public static boolean isNotEmpty(Collection<?> collection)
    {
    	if(collection == null || collection.size() == 0)
    	{
    		return false;
    	}
    	return true;
    }
    
    /**  
	 * @Title: getDiffData
	 * @Description: 获取集合差值
	 */
	public static List<String> getDiffData(List<String> list1,List<String> list2)
	{
		List<String> newlist1 = new ArrayList<String>();
		newlist1.addAll(list1);
		List<String> newlist2 = new ArrayList<String>();
		newlist2.addAll(list2);
		newlist1.removeAll(newlist2);
		return newlist1;
	}
	
	/**  
	 * @MethodName: isStringContainsListElement
	 * @Description: 判断字符串中是否包含数组中的某个元素
	 */
	public static boolean isStringContainsListElement(String str,List<List<String>> list) {
		boolean result = false;
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).size(); j++) {
					if(str.contains(String.valueOf(list.get(i).get(j)).split("\\.")[0]) && str.contains(String.valueOf(list.get(i).get(j)).split("\\.")[1]))
					{
						result = true;
						break;
					}
					if(result)
					{
						break;
					}
				}
				
			}
		}
		return result;
	}
	
	/**  
	 * @MethodName: getFormulaDatasetName
	 * @Description: 获取公式中的数据集名称，公式中的必须是同一个数据集
	 */
	public static String getFormulaDatasetName(String str,List<List<String>> list)
	{
		String result = null;
		if(!ListUtil.isEmpty(list))
		{
			for (int i = 0; i < list.size(); i++) {
				List<String> columns = list.get(i);
				for (int j = 0; j < columns.size(); j++) {
					if(str.contains(columns.get(j).split("-")[1]))
					{
						if(StringUtil.isNullOrEmpty(result))
						{
							result = String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						}else {
							result = result + "," + String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						}
					}
				}
			}
		}
		return result;
	}
	
	public static Map<String, String> getNewCellValue(String str,List<List<String>> list,Map<String, String> datasetNameIdMap)
	{
		Map<String, String> result = new HashMap<>();
		String datasetNames = "";
		String cellValue = str;
 		if(!ListUtil.isEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				List<String> columns = list.get(i);
				for (int j = 0; j < columns.size(); j++) {
					if(str.contains(columns.get(j).split("-")[1])) {
						String datasetName = String.valueOf(columns.get(j).split("-")[1]).split("\\.")[0];
						String newDatasetName = datasetName;
						if(datasetNameIdMap.containsKey(datasetName))
						{
							newDatasetName = datasetNameIdMap.get(datasetName)+"-"+newDatasetName;
						}
						if(StringUtil.isNullOrEmpty(datasetNames))
						{
							datasetNames = newDatasetName;
						}else {
							datasetNames = datasetNames + "," + newDatasetName;
						}
						cellValue = cellValue.replaceAll(datasetName+"\\.", newDatasetName+"\\.");
						break;
					}
				}
			}
		}
 		result.put("cellValue", cellValue);
 		result.put("datasetNames", datasetNames);
		return result;
	}
	
	public static float getSimilarityRatio(String str, String target) {

		int d[][]; // 矩阵
		int n = str.length();
		int m = target.length();
		int i; // 遍历str的
		int j; // 遍历target的
		char ch1; // str的
		char ch2; // target的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0 || m == 0) {
			return 0;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++) { // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++) {
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1), d[i - 1][j - 1] + temp);
			}
		}

		return (1 - (float) d[n][m] / Math.max(str.length(), target.length())) * 100F;
	}
	
	/**  
	 * @MethodName: getSerialNumList
	 * @Description: 获取列表中多组连续数据
	 */
	public static List<List<Integer>> getSerialNumList(List<Integer> list) {
	    List<List<Integer>> resultList = new ArrayList<>();
	    List<Integer> arrList = new ArrayList<>();
	    resultList.add(arrList);
	    if (list.size() == 1) {
	        resultList.get(resultList.size() - 1).add(list.get(0));
	        return resultList;
	    }
	    for (int i = 0; i < list.size(); i++) {
	        Integer nextNum = list.get(i + 1);
	        Integer nowNum = list.get(i);
	        if (nextNum - nowNum != 1) {
	            resultList.get(resultList.size() - 1).add(nowNum);
	            arrList = new ArrayList<>();
	            resultList.add(arrList);
	        } else {
	            arrList.add(nowNum);
	        }
	        if (i + 1 == list.size() - 1 ) {
	            arrList.add(nextNum);
	            break;
	        }
	    }
	    return resultList;
	}
	
	/**  
	 * @MethodName: getSubList
	 * @Description: list手动分页
	 */
	public static List<Map<String, Object>> getSubList(List<Map<String, Object>> rows,Integer pageNum,Integer pageSize)
	{
		List<Map<String, Object>> result = null;
		int startIndex = 0;
        int endIndex = rows.size();
        if (pageNum != null && pageSize != null) {
            startIndex = getStartIndex(pageNum, pageSize);
            endIndex = getEndIndex(pageNum, pageSize);
        }
        if (rows.size() >= endIndex) {
        	result = rows.subList(startIndex, endIndex);
        } else {
        	result = rows.subList(startIndex, rows.size());
        }
        return result;
	}
	

/**
	 * 获取起始位置
	 * @return
	 */
	public static int getStartIndex(int pageNum,int pageSize){
		return 0+(pageNum-1)*pageSize;
	}
	/**
	 * 获取结束位置
	 * @return
	 */
	public static int getEndIndex(int pageNum,int pageSize){
		return pageNum*pageSize;
	}
	
	public static void main(String[] args) {
		 Integer[] arr = {1,2, 3, 4, 6, 8, 10, 11, 12, 15, 16};
		    List<Integer> list = new ArrayList<>(Arrays.asList(arr));
		    List<List<Integer>> serialNumList = getSerialNumList(list);
		    serialNumList.forEach(System.out::println);
	}
}
