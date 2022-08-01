package com.bobocode.svydovets.beans.scanner;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public abstract class AbstractBeanScanner implements BeanScanner {

    protected String getTypeName(Class<?> type) {
        Objects.requireNonNull(type);

        return StringUtils.uncapitalize(type.getName());
    }

    protected static String findDependsOnByClass(Map<String, BeanDefinition> beanDefinitions, Class<?> beanClass,
                                             Class<?> typeToInject) {
        var typeName = StringUtils.uncapitalize(typeToInject.getName());
        if (beanDefinitions.containsKey(typeName)) {
            return typeName;
        }

        var foundTypes = beanDefinitions.entrySet().stream()
                .filter(e -> ClassUtils.isAssignable(e.getValue().getBeanClass(), typeToInject))
                .toList();

        if (foundTypes.isEmpty()) {
            throw new NoSuchBeanDefinitionException(String.format("Cannot found bean '%s' to inject into bean `%s`",
                    typeToInject.getSimpleName(), beanClass.getSimpleName()));
        } else if (foundTypes.size() > 1) {
            throw new NoUniqueBeanDefinitionException(String.format(
                    "There were found %d beans of the '%s' to inject into bean '%s'",
                    foundTypes.size(), typeToInject.getSimpleName(), beanClass.getSimpleName()));
        } else {
            return foundTypes.get(0).getKey();
        }
    }

    protected static String findDependsOnByName(Map<String, BeanDefinition> beanDefinitions, String beanName) {
        if (beanDefinitions.containsKey(beanName )) {
            return beanName;
        } else {
            throw new NoSuchBeanDefinitionException(
                    String.format("There is no such bean with name '%s'", beanName));
        }
    }
}
