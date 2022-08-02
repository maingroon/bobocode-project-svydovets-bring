package com.bobocode.svydovets.beans.scanner;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractBeanScanner implements BeanScanner {

    protected String getTypeName(Class<?> type) {
        Objects.requireNonNull(type);

        return StringUtils.uncapitalize(type.getName());
    }
}
