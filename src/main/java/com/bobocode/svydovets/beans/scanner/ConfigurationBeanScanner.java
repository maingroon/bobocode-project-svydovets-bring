package com.bobocode.svydovets.beans.scanner;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import org.reflections.Reflections;

import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {

    /**
     * This method scan package to find classes annotated with {@link Configuration} annotation
     * and returns bean definition for these classes.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean definition
     */
    @Override
    public Map<String, BeanDefinition> scan(String packageName) {
        return new Reflections(packageName)
                .getTypesAnnotatedWith(Configuration.class)
                .stream()
                .map(this::getConfigurationBeanDefinition)
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        beanDefinition -> beanDefinition
                ));
    }

    private BeanDefinition getConfigurationBeanDefinition(Class<?> type) {
        var name = type.getAnnotation(Configuration.class).value();
        name = name.isEmpty() ? type.getName() : name;

        return new BeanDefinition(name, type);
    }
}
