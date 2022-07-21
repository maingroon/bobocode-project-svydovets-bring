package com.bobocode.svydovets.beans.factory;

import java.util.Map;
import java.util.Set;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public interface BeanFactory {

    Object createBean(String name);

    <T> T createBean(Class<T> beanType);

    <T> T createBean(String name, Class<T> beanType);

    <T> T createBean(Map<String, BeanDefinition> nameToBeanDefinition);

    Map<String, Object> createBeans(Class<?> beanType);

    Map<String, Object> create(Set<Class<?>> beanTypes);

    void setNameToBeanDefinitionMap(Map<String, BeanDefinition> scan);
}
