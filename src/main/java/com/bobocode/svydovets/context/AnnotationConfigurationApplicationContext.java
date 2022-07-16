package com.bobocode.svydovets.context;

import java.util.Map;

import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContext implements ApplicationContext {

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        return null;
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public Map<String, Object> getBeans(Class<?> beanType) throws NoSuchBeanDefinitionException {
        return null;
    }
}
