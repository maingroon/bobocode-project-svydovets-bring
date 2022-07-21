package com.bobocode.svydovets.beans.scanner;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.bobocode.svydovets.context.AnnotationConfigurationApplicationContext;
import com.bobocode.svydovets.context.ApplicationContext;
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
        return new Reflections(packageName)
                .getTypesAnnotatedWith(Component.class)
                .stream()
                .map(type -> {
                    var name = type.getAnnotation(Component.class).value();
                    name = name.isEmpty() ? type.getName() : name;

                    return new BeanDefinition(name, type);
                })
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        beanDefinition -> beanDefinition
                ));
    }
}
