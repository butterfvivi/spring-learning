package org.vivi.framework.excel.dynamic3.utils;

import com.alibaba.excel.EasyExcelFactory;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.vivi.framework.excel.dynamic3.listener.DynamicEasyExcelListener;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 编写导入工具类
 */
public class DynamicEasyExcelImportUtils {
    /**
     * 动态获取全部列和数据体，默认从第一行开始解析数据
     * @param stream
     * @return
     */
    public static List<Map<String,String>> parseExcelToView(byte[] stream) {
        return parseExcelToView(stream, 1);
    }
    /**
     * 动态获取全部列和数据体
     * @param stream           excel文件流
     * @param parseRowNumber   指定读取行
     * @return
     */
    public static List<Map<String,String>> parseExcelToView(byte[] stream, Integer parseRowNumber) {
        DynamicEasyExcelListener readListener = new DynamicEasyExcelListener();
        EasyExcelFactory.read(new ByteArrayInputStream(stream)).registerReadListener(readListener).headRowNumber(parseRowNumber).sheet(0).doRead();
        List<Map<Integer, String>> headList = readListener.getHeadList();
        if(CollectionUtils.isEmpty(headList)){
            throw new RuntimeException("Excel未包含表头");
        }
        List<Map<Integer, String>> dataList = readListener.getDataList();
        if(CollectionUtils.isEmpty(dataList)){
            throw new RuntimeException("Excel未包含数据");
        }
        //获取头部,取最后一次解析的列头数据
        Map<Integer, String> excelHeadIdxNameMap = headList.get(headList.size() -1);
        //封装数据体
        List<Map<String,String>> excelDataList = Lists.newArrayList();
        for (Map<Integer, String> dataRow : dataList) {
            Map<String,String> rowData = new LinkedHashMap<>();
            excelHeadIdxNameMap.entrySet().forEach(columnHead -> {
                rowData.put(columnHead.getValue(), dataRow.get(columnHead.getKey()));
            });
            excelDataList.add(rowData);
        }
        return excelDataList;
    }

}
