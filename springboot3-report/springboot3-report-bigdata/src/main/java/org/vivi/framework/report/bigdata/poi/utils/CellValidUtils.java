package org.vivi.framework.report.bigdata.poi.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vivi.framework.report.bigdata.poi.model.RowErrorInfo;
import org.vivi.framework.report.bigdata.poi.model.SheetInfo;

import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellValidUtils {

    /**
     * 数据对象,hibernate-validate校验
     **/
    public static <T> void validExcelLineData(SheetInfo sheetInfo, Integer rowNum, T t) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations != null && !violations.isEmpty()) {
            RowErrorInfo errorInfo = null;
            if( sheetInfo.getErrorInfos().size() - 1 > rowNum ){
                errorInfo = sheetInfo.getErrorInfos().get(rowNum);
            }
            if( null == errorInfo ){
                errorInfo = new RowErrorInfo();
                sheetInfo.getErrorInfos().put(rowNum,errorInfo);
            }
            for (ConstraintViolation<T> info : violations) {
                errorInfo.getColumnErrors().add(info.getMessage());
            }
        }
    }
}
