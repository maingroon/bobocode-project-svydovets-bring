package com.bobocode.svydovets.beans.scanner;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.beans.definition.BeanDefinition;

public class ComponentBeanScanner implements BeanScanner {

    /**
     * This method scan package to find classes annotated with @Component annotation
     * and returns bean definition for this classes.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean d
     */
    @Override
    public Map<String, BeanDefinition> scan(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        return typesAnnotatedWith.stream()
                .map(type -> {
                    var name = type.getAnnotation(Component.class).value();
                    name = name.isEmpty() ? type.getName() : name;

                    return new BeanDefinition(name, type);
                })
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        bd -> bd
                ));
    }
}
