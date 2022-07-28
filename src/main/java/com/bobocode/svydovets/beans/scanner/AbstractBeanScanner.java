package com.bobocode.svydovets.beans.scanner;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public abstract class AbstractBeanScanner implements BeanScanner {

    protected String getTypeName(Class<?> type) {
        Objects.requireNonNull(type);

        return StringUtils.uncapitalize(type.getSimpleName());
    }
}
