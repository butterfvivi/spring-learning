package org.vivi.framework.report.bigdata.utils.poi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.vivi.framework.report.bigdata.entity.model.RowErrorInfo;
import org.vivi.framework.report.bigdata.entity.model.SheetInfo;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
