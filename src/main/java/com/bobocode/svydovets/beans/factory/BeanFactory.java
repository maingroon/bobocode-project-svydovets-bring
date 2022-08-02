package com.bobocode.svydovets.beans.factory;

import java.util.Map;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public interface BeanFactory {

    Map<String, Object> createBeans(Map<String, BeanDefinition> nameToBeanDefinition);
}
