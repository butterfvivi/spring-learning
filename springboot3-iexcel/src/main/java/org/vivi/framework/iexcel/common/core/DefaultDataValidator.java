package org.vivi.framework.iexcel.common.core;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

public class DefaultDataValidator<T> extends AbstractDataValidator<T> {

    public DefaultDataValidator() {
    }

    public DefaultDataValidator(Validator validator) {
        super(validator);
    }

    @Override
    public ValidationResult format(Set<ConstraintViolation<T>> violations) {
        if (violations == null || violations.isEmpty()) {
            return ValidationResult.success();
        }
        String errorMsg = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
        return ValidationResult.fail(errorMsg);
    }
}
