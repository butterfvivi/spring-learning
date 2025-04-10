package org.vivi.framework.excel.dynamic3.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.*;

/**
 * 创建一个文件读取监听器
 */
@Slf4j
public class DynamicEasyExcelListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * 表头数据（存储所有的表头数据）
     */
    private List<Map<Integer, String>> headList = new ArrayList<>();
    /**
     * 数据体
     */
    private List<Map<Integer, String>> dataList = new ArrayList<>();
    /**
     * 这里会一行行的返回头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JSON.toJSONString(headMap));
        //存储全部表头数据
        headList.add(headMap);
    }
    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        dataList.add(data);
    }
    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("所有数据解析完成！");
    }
    public List<Map<Integer, String>> getHeadList() {
        return headList;
    }
    public List<Map<Integer, String>> getDataList() {
        return dataList;
    }
}
