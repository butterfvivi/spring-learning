package org.vivi.framework.iexcel.common.core;


@FunctionalInterface
public interface DataValidator<T> {

    /**
     * Based on Hibernate Validator, validate Excel row data
     *
     * @param rowData excel row data
     * @return validation result
     */
    ValidationResult validate(T rowData);
}
