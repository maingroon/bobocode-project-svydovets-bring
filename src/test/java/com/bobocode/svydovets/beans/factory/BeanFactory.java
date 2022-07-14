package com.bobocode.svydovets.beans.factory;

import com.bobocode.svydovets.beans.BeanDefinition;

import java.util.Map;
import java.util.Set;

public interface BeanFactory {

    <T> T createBean(Class<T> beanType);

    <T> T createBean(Map<String, BeanDefinition> nameToBeanDefinition);

    Map<String, Object> create(Set<Class<?>> beanTypes);
}
