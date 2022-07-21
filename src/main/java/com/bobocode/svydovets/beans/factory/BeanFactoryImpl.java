package com.bobocode.svydovets.beans.factory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

public class BeanFactoryImpl implements BeanFactory {
    private final Map<String, BeanDefinition> nameToBeanDefinition;

    public BeanFactoryImpl() {
        this.nameToBeanDefinition = new ConcurrentHashMap<>();
    }

    @Override
    public Object createBean(String name) {
        return this.createBean(name, Object.class);
    }

    @Override
    public <T> T createBean(Class<T> beanType) {
        return null;
    }

    @Override
    public <T> T createBean(String name, Class<T> beanType) {
        return null;
    }

    @Override
    public <T> T createBean(Map<String, BeanDefinition> nameToBeanDefinition) {
        return null;
    }

    @Override
    public Map<String, Object> createBeans(Class<?> beanType) {
        return null;
    }

    @Override
    public Map<String, Object> create(Set<Class<?>> beanTypes) {
        return null;
    }

    @Override
    public void setNameToBeanDefinitionMap(Map<String, BeanDefinition> map) {
        nameToBeanDefinition.putAll(map);
    }

}
