package org.vivi.framework.iexcel.common.read.listener;

import com.alibaba.excel.context.AnalysisContext;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iexcel.common.context.ReadContext;
import org.vivi.framework.iexcel.common.read.BasedExcelReadModel;
import org.vivi.framework.iexcel.common.read.ExcelReadParam;
import org.vivi.framework.iexcel.common.read.ExcelReader;
import org.vivi.framework.iexcel.common.support.ExcelCommonException;
import org.vivi.framework.iexcel.common.util.ExcelHeadUtils;
import org.vivi.framework.iexcel.common.util.JsonUtils;
import org.vivi.framework.iexcel.common.context.holder.ContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
@Getter
@Slf4j
public class ExcelReadListener<T extends BasedExcelReadModel> extends AbstractExcelReadListener<T> {

    private final ExcelReadParam readParam;

    private final List<T> dataCache = new ArrayList<>();

    public ExcelReadListener(ExcelReadParam readParam) {
        this.readParam = readParam;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("Row data: {}", JsonUtils.toJsonString(data));
        setReadContext(context);

        ExcelReader<T> excelReader = (ExcelReader<T>) readParam.getExcelReader();
        BasedExcelReadModel validation = new BasedExcelReadModel();

        // pre-check
        if (readParam.isCheckCacheRepeat() && dataCache.contains(data)) {

            validation.setAvailable(false);
            validation.setMsg(BasedExcelReadModel.DATA_REPEAT);

        } else if (readParam.isPreCheck()) {

            validation = excelReader.check(data);
        }
        data.setAvailable(validation.getAvailable());
        data.setMsg(validation.getMsg());

        // add to the cache, reach the specified number to save
        dataCache.add(data);
        if (dataCache.size() >= readParam.getBatchCount()) {
            excelReader.read(dataCache, context);
            dataCache.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!dataCache.isEmpty()) {
            ExcelReader<T> excelReader = (ExcelReader<T>) readParam.getExcelReader();
            excelReader.read(dataCache, context);
        }
    }

    @Override
    protected ContextHolder<String, ReadContext> contextHolder() {
        return readParam.getContextHolder();
    }

    @Override
    protected String key() {
        return readParam.getKey();
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        boolean isHead = context.readSheetHolder().getHeadRowNumber() - 1 == context.readRowHolder().getRowIndex();
        if (isHead && readParam.isCheckHead()) {
            log.info("Head data: {}", JsonUtils.toJsonString(headMap));

            Map<Integer, String> predefinedHeadMap = new HashMap<>(headMap.size());
            context.currentReadHolder()
                    .excelReadHeadProperty()
                    .getHeadMap()
                    .forEach((index, head) -> predefinedHeadMap.put(index, head.getHeadNameList().get(0)));

            if (!ExcelHeadUtils.same(predefinedHeadMap, headMap)) {
                throw new ExcelCommonException("The Excel header does not match the template!");
            }
        }
    }
}
