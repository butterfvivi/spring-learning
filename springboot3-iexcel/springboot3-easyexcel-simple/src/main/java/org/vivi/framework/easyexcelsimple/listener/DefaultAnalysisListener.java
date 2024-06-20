package org.vivi.framework.easyexcelsimple.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultAnalysisListener<E> extends AnalysisEventListener<E> {

    private final List<E> list = new ArrayList<>();

    public DefaultAnalysisListener() {

    }

    @Override
    public void invoke(E object, AnalysisContext context) {
        list.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("{}条数据解析完成!", list.size());
    }

    public List<E> getList() {
        return list;
    }

}
