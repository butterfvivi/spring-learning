package org.vivi.framework.iexcel.common.core;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;


public abstract class AbstractDataValidator<T> implements DataValidator<T> {

    protected final Validator validator;

    public AbstractDataValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public AbstractDataValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public ValidationResult validate(T excelData) {
        Set<ConstraintViolation<T>> violations = validator.validate(excelData);
        return format(violations);
    }

    /**
     * Format violation info
     *
     * @param violations violation info
     * @return validation result
     */
    protected abstract ValidationResult format(Set<ConstraintViolation<T>> violations);
}
