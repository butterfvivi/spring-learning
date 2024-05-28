package org.vivi.framework.iexcel.common.readv2;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;

import lombok.extern.slf4j.Slf4j;
import org.vivi.framework.iexcel.common.core.DataValidator;
import org.vivi.framework.iexcel.common.core.ValidationResult;
import org.vivi.framework.iexcel.common.support.ExcelHeadParseException;
import org.vivi.framework.iexcel.common.util.ExcelHeadUtils;
import org.vivi.framework.iexcel.common.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public abstract class AbstractReadListener<T> implements ReadListener<T> {

    private final ReadParam<T> readParam;

    public AbstractReadListener(ReadParam<T> readParam) {
        this.readParam = readParam;
    }

    @Override
    public void invoke(T rowData, AnalysisContext analysisContext) {
        log.info("Row data: {}", JsonUtils.toJsonString(rowData));
        // set to read context

        // validate row data
        ValidationResult validationResult = validate(rowData);
        doInvoke(rowData, validationResult, analysisContext);
    }

    protected abstract void doInvoke(T rowData, ValidationResult validationResult, AnalysisContext analysisContext);


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // nothing
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> headStringMap = ConverterUtils.convertToStringMap(headMap, context);
        boolean isHead = context.readSheetHolder().getHeadRowNumber() - 1 == context.readRowHolder().getRowIndex();
        if (isHead && readParam.isCheckHead()) {
            log.info("Head data: {}", JsonUtils.toJsonString(headMap));

            Map<Integer, String> templatedHeadMap = new HashMap<>(headMap.size());
            context.currentReadHolder()
                    .excelReadHeadProperty()
                    .getHeadMap()
                    .forEach((index, head) -> templatedHeadMap.put(index, head.getHeadNameList().get(0)));

            if (!ExcelHeadUtils.same(templatedHeadMap, headStringMap)) {
                throw new ExcelHeadParseException("Excel header does not match the template!");
            }
        }
    }

    private ValidationResult validate(T rowData) {
        DataValidator<T> validator = readParam.getValidator();
        return validator == null ? ValidationResult.success() : validator.validate(rowData);
    }
}
