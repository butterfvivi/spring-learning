package org.vivi.framework.iasyncexcel.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.xlsx.XlsxReadSheetHolder;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.vivi.framework.iasyncexcel.common.exception.CustomException;
import org.vivi.framework.iasyncexcel.core.importer.ISheetRow;
import org.vivi.framework.iasyncexcel.core.importer.ImportContext;
import org.vivi.framework.iasyncexcel.core.importer.ImportRowMap;
import org.vivi.framework.iasyncexcel.core.importer.ImportTaskSupport;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AsyncReadListener<T> implements ReadListener<T> {

    private int batchSize = 100;
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(batchSize);

    private final Consumer<List<T>> consumer;

    private ImportContext ctx;

    private Map<Integer, String> headMap;

    private ImportTaskSupport support;

    public AsyncReadListener(Consumer<List<T>> consumer,ImportTaskSupport support, ImportContext ctx,
                                 int batchSize) {
        if (batchSize > 0) {
            this.batchSize = batchSize;
        }
        this.ctx = ctx;
        this.consumer = consumer;
        this.support = support;
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> readHead, AnalysisContext context) {
        this.headMap = ConverterUtils.convertToStringMap(readHead, context);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        if (StringUtils.isBlank(ctx.getSheetName())) {
            String sheetName = ((XlsxReadSheetHolder) context
                    .currentReadHolder()).getSheetName();
            ctx.setSheetName(sheetName);
        }
        Integer rowIndex = context.readRowHolder().getRowIndex();
        //20221025 添加动态表头支持
        if (data instanceof ImportRowMap){
            ImportRowMap rowMap = (ImportRowMap) data;
            rowMap.setDataMap(context.readRowHolder().getCellMap());
            rowMap.setHeadMap(headMap);
        }

//        if (data instanceof ISheetRow) {
//            ISheetRow rowData = (ISheetRow) data;
//            rowData.setRow(rowIndex);
//        } else {
//            throw new RuntimeException("导入对应实体必须继承ISheetRow");
//        }
        if (ctx.getTask().getEstimateCount() == 0L) {
            Integer headRowNumber = context.readSheetHolder().getHeadRowNumber();
            Integer totalCount = context.getTotalCount() - headRowNumber;
            ctx.getTask().setEstimateCount(totalCount.longValue());
        }
        if (ctx.isValidMaxRows()){
            if (ctx.getTask().getEstimateCount()>ctx.getMaxRows()){
                throw new CustomException("行数限制{"+ctx.getMaxRows()+"}行,包含表头与空行");
            }
        }
        cachedDataList.add(data);
        if (cachedDataList.size() >= this.batchSize) {
            consumer.accept(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(this.batchSize);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            consumer.accept(cachedDataList);
        }
    }
}
