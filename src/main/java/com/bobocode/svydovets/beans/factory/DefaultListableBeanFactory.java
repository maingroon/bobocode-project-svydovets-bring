package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

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
        return definitionMap.entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> createBean(entry.getValue())));
    }

    private static Object createBean(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.getBeanClass().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException
          | IllegalAccessException | NoSuchMethodException e) {
            throw new BeanInstantiationException(e.getMessage());
        }
    }

}
