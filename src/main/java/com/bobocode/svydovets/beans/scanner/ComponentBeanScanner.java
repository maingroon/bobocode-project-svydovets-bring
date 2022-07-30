package com.bobocode.svydovets.beans.scanner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
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
        for (BeanDefinition beanDefinition : beanDefinitions.values()) {
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (beanClass == null) {
                continue;
            }

            beanDefinition.setDependsOn(findDependsOn(beanDefinitions, beanClass));
        }
    }

    private static String[] findDependsOn(Map<String, BeanDefinition> beanDefinitions, Class<?> beanClass) {
        var dependsOn = new ArrayList<>();
        for (Field field : beanClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            String beanName = field.getAnnotation(Inject.class).value();
            if (StringUtils.isNotEmpty(beanName)) {
                findDepnesOnByName(beanDefinitions, dependsOn, beanName);
            } else {
                findDependsOnByClass(beanDefinitions, beanClass, dependsOn, field);
            }
        }

        return dependsOn.toArray(new String[0]);
    }

    private static void findDepnesOnByName(Map<String, BeanDefinition> beanDefinitions,
                                           ArrayList<Object> dependsOn, String beanName) {
        if (beanDefinitions.containsKey(beanName)) {
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
