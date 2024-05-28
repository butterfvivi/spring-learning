package org.vivi.framework.iexcel.common.read;

import com.alibaba.excel.context.AnalysisContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ExcelReader<T> {

    Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    void read(List<T> excelDataList, AnalysisContext context);

    default BasedExcelReadModel check(T excelData) {
        BasedExcelReadModel validation = new BasedExcelReadModel();
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(excelData);
        if (validate != null && !validate.isEmpty()) {
            List<String> failureMsg = validate.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
            validation.setAvailable(false);
            validation.setMsg(String.join(";", failureMsg));
        } else {
            validation.setAvailable(true);
        }
        return validation;
    }
}
