package com.bobocode.svydovets.beans.factory;

import java.util.Map;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

/**
 * The interface for creation a number of beans based on
 * their bean definitions. Each bean identified by a String name
 */
public interface BeanFactory {

    /**
     * Return a map of bean objects created from bean definitions
     * @param nameToBeanDefinition bean definition map, key - String name of the bean,
     * value - bean definition
     * @return bean map, where key - String bean name, and value - Object bean
     */
    Map<String, Object> createBeans(Map<String, BeanDefinition> nameToBeanDefinition);
}
