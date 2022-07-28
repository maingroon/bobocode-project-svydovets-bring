package com.bobocode.svydovets.beans.scanner;

import java.util.Objects;

public abstract class AbstractBeanScanner implements BeanScanner {

    protected String getTypeName(Class<?> type) {
        Objects.requireNonNull(type);

        var simpleName = type.getSimpleName();
        if (simpleName.length() <= 1) {
            return simpleName;
        }
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
