package com.bobocode.svydovets.beans.factory;

import java.util.Map;
import java.util.Set;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public interface BeanFactory {

    <T> T createBean(Class<T> beanType);

    <T> T createBean(Map<String, BeanDefinition> nameToBeanDefinition);

    Map<String, Object> create(Set<Class<?>> beanTypes);

    Object createBean(String name);

    <T> T createBean(String name, Class<T> beanType);

    Map<String, Object> createBeans(Class<?> beanType);

    void setNameToBeanDefinitionMap(Map<String, BeanDefinition> scan);
}
