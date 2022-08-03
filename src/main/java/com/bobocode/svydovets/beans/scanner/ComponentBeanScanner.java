package com.bobocode.svydovets.beans.scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.definition.BeanDefinition;

public class ComponentBeanScanner extends AbstractBeanScanner {

    /**
     * This method scan package to find classes annotated with @Component annotation
     * and returns bean definition for these classes.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean definition
     */
    @Override
    public Map<String, BeanDefinition> scan(String packageName) {
        return new Reflections(packageName)
                .getTypesAnnotatedWith(Component.class)
                .stream()
                .map(type -> {
                    var name = type.getAnnotation(Component.class).value();
                    name = name.isEmpty() ? getTypeName(type) : name;

                    return new BeanDefinition(name, type);
                })
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        beanDefinition -> beanDefinition
                ));
    }

    @Override
    public void fillDependsOn(Map<String, BeanDefinition> beanDefinitions) {
        beanDefinitions.values().stream()
                .filter(beanDefinition -> Objects.nonNull(beanDefinition.getBeanClass()))
                .forEach(beanDefinition -> beanDefinition.setDependsOn(
                        findDependsOn(beanDefinitions, beanDefinition.getBeanClass())));
    }

    private static String[] findDependsOn(Map<String, BeanDefinition> beanDefinitions, Class<?> beanClass) {
        var dependsOn = new ArrayList<>();

        Arrays.stream(beanClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    String beanName = field.getAnnotation(Inject.class).value();
                    if (StringUtils.isNotEmpty(beanName)) {
                        dependsOn.add(findDependsOnByName(beanDefinitions, beanName));
                    } else {
                        dependsOn.add(findDependsOnByClass(beanDefinitions, beanClass, field.getType()));
                    }
                });

        return dependsOn.toArray(new String[0]);
    }
}
