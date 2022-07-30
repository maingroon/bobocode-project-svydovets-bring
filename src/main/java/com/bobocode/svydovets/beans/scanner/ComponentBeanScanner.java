package com.bobocode.svydovets.beans.scanner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

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
                        findDependsOnByName(beanDefinitions, dependsOn, beanName);
                    } else {
                        findDependsOnByClass(beanDefinitions, beanClass, dependsOn, field);
                    }
                });

        return dependsOn.toArray(new String[0]);
    }

    private static void findDependsOnByName(Map<String, BeanDefinition> beanDefinitions,
                                            ArrayList<Object> dependsOn, String beanName) {
        if (beanDefinitions.containsKey(beanName )) {
            dependsOn.add(beanName);
        } else {
            throw new NoSuchBeanDefinitionException(
                    String.format("There is no such bean with name '%s'", beanName));
        }
    }

    private static void findDependsOnByClass(Map<String, BeanDefinition> beanDefinitions, Class<?> beanClass,
                                             ArrayList<Object> dependsOn, Field field) {
        Class<?> typeToInject = field.getType();
        var typeName = StringUtils.uncapitalize(typeToInject.getName());
        if (beanDefinitions.containsKey(typeName)) {
            dependsOn.add(typeName);
            return;
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
            dependsOn.add(foundTypes.get(0).getKey());
        }
    }
}
