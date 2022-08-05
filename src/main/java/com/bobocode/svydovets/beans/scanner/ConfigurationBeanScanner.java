package com.bobocode.svydovets.beans.scanner;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import com.bobocode.svydovets.annotation.Bean;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import com.bobocode.svydovets.exception.UnsupportedBeanTypeException;

/**
 * A bean definition scanner that detects bean candidates in configuration classes.
 */
public class ConfigurationBeanScanner extends AbstractBeanScanner {

    /**
     * This method scans package to find classes annotated with the @Configuration annotation
     * and scans all methods annotated with the @Bean annotation within a provided configuration class.
     * Returns bean definition for found methods and configuration classes annotated with appropriate annotations.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean definitions
     * @throws NoUniqueBeanDefinitionException in case of duplicating bean names
     * @throws UnsupportedBeanTypeException    in case a void method marked with a @Bean annotation
     */
    @Override
    public Map<String, BeanDefinition> scan(String packageName) {

        var configurations = new Reflections(packageName)
          .getTypesAnnotatedWith(Configuration.class)
          .stream()
          .map(this::getBeanDefinition)
          .collect(toMap(BeanDefinition::getName, Function.identity()));

        var beans = configurations.values().stream()
          .flatMap(confClass -> Arrays.stream(confClass.getBeanClass().getMethods()))
          .filter(method -> method.isAnnotationPresent(Bean.class))
          .map(this::getBeanDefinition)
          .collect(toMap(BeanDefinition::getName, Function.identity()));

        try {
            return Stream.of(configurations, beans)
              .flatMap(map -> map.entrySet().stream())
              .collect(Collectors
                .toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IllegalStateException ex) {
            throw createUniqueBeanDefinitionException(ex);
        }
    }

    private NoUniqueBeanDefinitionException createUniqueBeanDefinitionException(IllegalStateException ex) {
        String errMsg = ex.getMessage();
        String editedMsg;
        if (StringUtils.isBlank(errMsg)) {
            editedMsg = "Duplicate bean found. Check possible duplicates under the @Component and @Bean annotations.";
        } else {
            // on map merge duplicate conflict, the message is:
            // "Duplicate key {beanName} (attempted merging values
            // com.bobocode.svydovets.beans.definition.BeanDefinition@2e140e59 and
            // com.bobocode.svydovets.beans.definition.BeanDefinition@49a71302)"
            // The info inside the brackets is redundant.
            String correctedMsg = errMsg.substring(0, errMsg.indexOf("(")).replace("key", "bean");
            editedMsg = correctedMsg.trim() + ". Check possible duplicates under the @Component and @Bean annotations.";
        }
        return new NoUniqueBeanDefinitionException(editedMsg);
    }

    @Override
    public void fillDependsOn(Map<String, BeanDefinition> beanDefinitions) {
        beanDefinitions.values().stream()
                .filter(bd -> Objects.nonNull(bd.getFactoryMethod()))
                .forEach(bd -> fillDependsOn(beanDefinitions, bd));
    }

    private void fillDependsOn(Map<String, BeanDefinition> beanDefinitions, BeanDefinition bd) {
        var dependsOn = new ArrayList<>();

        var parameterTypes = bd.getFactoryMethod().getParameterTypes();
        Arrays.stream(parameterTypes)
                .forEach(type -> dependsOn.add(findDependsOnByClass(beanDefinitions, bd.getBeanClass(), type)));

        bd.setDependsOn(dependsOn.toArray(new String[0]));
    }

    /**
     * Constructs a BeanDefinition instance based on configuration class parameter.
     *
     * @param confClass - class annotated with @Configuration annotation
     * @return a new instance of a BeanDefinition
     */
    private BeanDefinition getBeanDefinition(Class<?> confClass) {
        var confName = confClass.getAnnotation(Configuration.class).value();
        confName = confName.isEmpty() ? getTypeName(confClass) : confName;
        return BeanDefinition.builder()
          .name(confName)
          .beanClass(confClass)
          .postConstructMethod(findPostConstructMethod(confClass))
          .build();
    }

    /**
     * Constructs a ConfigurationBeanDefinition instance based on bean method parameter.
     *
     * @param beanMethod - a method annotated with @Bean annotation
     * @return a new instance of a ConfigurationBeanDefinition
     */
    private BeanDefinition getBeanDefinition(Method beanMethod) {
        var beanName = beanMethod.getAnnotation(Bean.class).value();
        var beanClass = beanMethod.getReturnType();
        if (beanClass.equals(Void.TYPE)) {
            throw new UnsupportedBeanTypeException(
              String.format("Bean: %s with the return type void could not be added to the context", beanName));
        }
        beanName = beanName.isEmpty() ? getTypeName(beanClass) : beanName;
        return BeanDefinition.builder()
          .name(beanName)
          .configurationClass(beanMethod.getDeclaringClass())
          .factoryMethod(beanMethod)
          .beanClass(beanMethod.getReturnType())
          .build();
    }
}
