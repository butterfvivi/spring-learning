package org.vivi.framework.iexcel;

import com.alibaba.excel.context.AnalysisContext;

import lombok.extern.slf4j.Slf4j;
import org.vivi.spring3.easyexcel.common.context.ReadContext;
import org.vivi.spring3.easyexcel.common.read.BasedExcelReadModel;
import org.vivi.spring3.easyexcel.common.read.ExcelReadParam;
import org.vivi.spring3.easyexcel.common.read.listener.ExcelReadListener;

/**
 * @author leslie
 * @date 2021/6/8
 */
@Slf4j
public class CustomExcelReadListener<T extends BasedExcelReadModel> extends ExcelReadListener<T> {

    public CustomExcelReadListener(ExcelReadParam readParam) {
        super(readParam);
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        log.warn("ReadContext: {}", contextHolder().getContext(key()).orElse(ReadContext.builder().build()));
        super.invoke(data, context);
    }
}
