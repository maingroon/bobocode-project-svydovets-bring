package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public interface BeanFactory {

    Map<String, Object> createBeans(Map<String, BeanDefinition> nameToBeanDefinition) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
