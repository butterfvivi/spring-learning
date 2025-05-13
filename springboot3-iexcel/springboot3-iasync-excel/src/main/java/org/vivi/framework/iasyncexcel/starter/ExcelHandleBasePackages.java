package org.vivi.framework.iasyncexcel.starter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ExcelHandleBasePackages {
    private final Set<String> basePackages;

    public ExcelHandleBasePackages(Set<String> basePackages) {
        Objects.requireNonNull(basePackages, "Base packages cannot be null");
        this.basePackages = Collections.unmodifiableSet(new HashSet<>(basePackages));
    }

    public Set<String> getBasePackages() {
        return basePackages;
    }
}

