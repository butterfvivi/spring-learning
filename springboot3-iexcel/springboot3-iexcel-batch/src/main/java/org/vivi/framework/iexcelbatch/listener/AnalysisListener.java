package org.vivi.framework.iexcelbatch.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AnalysisListener<T> extends AnalysisEventListener<T> {

    /**
     * 分批数量
     */
    private int batchSize;

    /**
     * 分批缓存的集合
     */
    private List<T> cachedDataList;

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= batchSize) {
            log.info("读取数据量:{}", cachedDataList.size());
            cachedDataList = new ArrayList(batchSize);
        }
    }

    /**
     * 所有数据读取完成之后调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            //处理剩余的数据
            log.info("读取数据量:{}", cachedDataList.size());
        }
    }
}