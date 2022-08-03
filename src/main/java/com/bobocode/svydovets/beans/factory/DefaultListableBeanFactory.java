package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.exception.BeanInstantiationException;

public class DefaultListableBeanFactory implements BeanFactory {

    /**
     * Create a new map of bean instances, from bean definitions.
     *
     * @param definitionMap bean definition map, key - the name of the bean, the value - bean definition object
     * @return bean map, where key - bean name, and value - new instance of the bean
     */
    @Override
    public Map<String, Object> createBeans(Map<String, BeanDefinition> definitionMap) {
        var componentBeans = definitionMap.values().stream()
          .filter(bd -> Objects.isNull(bd.getFactoryMethod()))
          .collect(Collectors.toMap(BeanDefinition::getName, DefaultListableBeanFactory::createComponentBean));

        var configurationDeclaredBeans = definitionMap.values().stream()
          .filter(bd -> Objects.nonNull(bd.getFactoryMethod()))
          .map(confDeclaredBeanDefinitionEntry -> createConfigurationDeclaredBean(confDeclaredBeanDefinitionEntry,
            componentBeans))
          .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        Map<String, Object> beanMap = Stream.of(componentBeans, configurationDeclaredBeans)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        injectAll(beanMap);

        return beanMap;
    }

    private static Pair<String, Object> createConfigurationDeclaredBean(BeanDefinition beanDefinition,
                                                                        Map<String, Object> componentBeans) {
        var declaredClass = beanDefinition.getConfigurationClass();
        var declaredClassConfigValue = declaredClass.getAnnotation(Configuration.class).value().trim();
        var componentBeanName =
          declaredClassConfigValue.isEmpty() ? StringUtils.uncapitalize(declaredClass.getName()) :
            declaredClassConfigValue;
        var declaredClassInstance = componentBeans.get(componentBeanName);
        if (beanDefinition.getFactoryMethod().getParameters().length > 0) {
            throw new UnsupportedOperationException(
              "Creating bean instance with other injected beans is not yet supported");
        }
        try {
            var beanInstance = beanDefinition.getFactoryMethod().invoke(declaredClassInstance,
              (Object[]) beanDefinition.getFactoryMethod().getParameters());
            return Pair.of(beanDefinition.getName(), beanInstance);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new BeanInstantiationException("Could not instantiate a bean.", ex);
        }
    }

    private static Object createComponentBean(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.getBeanClass().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException exception) {
            throw new BeanInstantiationException(exception.getMessage());
        }
    }

    private void injectAll(Map<String, Object> beanMap) {
        beanMap.values().forEach(bean ->
          Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> inject(bean, field, beanMap)));
    }

    private void inject(Object bean, Field field, Map<String, Object> beanMap) {
        Inject fieldAnnotation = field.getAnnotation(Inject.class);

        if (StringUtils.isNotEmpty(fieldAnnotation.value())) {
            injectToFiled(bean, field, beanMap.get(fieldAnnotation.value()));
        } else {
            injectToFiled(bean, field, beanMap.get(field.getType().getName()));
        }
    }

    private void injectToFiled(Object bean, Field field, Object injectBean) {
        try {
            field.setAccessible(true);
            field.set(bean, injectBean);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(String.format("Unable to inject '%s' into '%s'. Fall with exception: [%s]",
              injectBean.getClass().getSimpleName(), bean.getClass().getSimpleName(), exception));
        }
    }
}
