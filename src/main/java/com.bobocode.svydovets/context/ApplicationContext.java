package com.bobocode.svydovets.context;

import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

import java.util.Map;

public interface ApplicationContext {

    <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException;

    Object getBean(String name) throws NoSuchBeanDefinitionException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanDefinitionException;

    Map<String, Object> getBeans(Class<?> beanType) throws NoSuchBeanDefinitionException;
}
