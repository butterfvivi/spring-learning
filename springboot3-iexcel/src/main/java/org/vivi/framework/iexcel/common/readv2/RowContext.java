package org.vivi.framework.iexcel.common.readv2;

import com.alibaba.excel.context.AnalysisContext;
import lombok.Getter;
import lombok.Setter;
import org.vivi.framework.iexcel.common.core.ValidationResult;

import java.io.Serializable;

@Getter
@Setter
public class RowContext<T> implements Serializable {

    private static final long serialVersionUID = 5372269490516565020L;

    private T rowData;
    private ValidationResult validation;
    private AnalysisContext analysisContext;

    public RowContext(T rowData, ValidationResult validation, AnalysisContext analysisContext) {
        this.rowData = rowData;
        this.validation = validation;
        this.analysisContext = analysisContext;
    }
}
