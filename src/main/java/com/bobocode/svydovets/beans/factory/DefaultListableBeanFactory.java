package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bobocode.svydovets.annotation.Configuration;
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
          .filter(bd -> Objects.isNull(bd.getBeanMethod()))
          .collect(Collectors.toMap(BeanDefinition::getName, DefaultListableBeanFactory::createComponentBean));

        var configurationDeclaredBeans = definitionMap.values().stream()
          .filter(bd -> Objects.nonNull(bd.getBeanMethod()))
          .map(confDeclaredBeanDefinitionEntry -> createConfigurationDeclaredBean(confDeclaredBeanDefinitionEntry,
            componentBeans))
          .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        return Stream.of(componentBeans, configurationDeclaredBeans)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors
            .toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Pair<String, Object> createConfigurationDeclaredBean(BeanDefinition beanDefinition,
                                                                        Map<String, Object> componentBeans) {
        var declaredClass = beanDefinition.getBeanClass();
        var declaredClassConfigValue = declaredClass.getAnnotation(Configuration.class).value().trim();
        var componentBeanName =
          declaredClassConfigValue.isEmpty() ? StringUtils.uncapitalize(declaredClass.getSimpleName()) :
            declaredClassConfigValue;
        var declaredClassInstance = componentBeans.get(componentBeanName);
        if (beanDefinition.getBeanMethod().getParameters().length > 0) {
            throw new UnsupportedOperationException(
              "Creating bean instance with other injected beans is not yet supported");
        }
        try {
            var beanInstance = beanDefinition.getBeanMethod().invoke(declaredClassInstance,
              (Object[]) beanDefinition.getBeanMethod().getParameters());
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
}
