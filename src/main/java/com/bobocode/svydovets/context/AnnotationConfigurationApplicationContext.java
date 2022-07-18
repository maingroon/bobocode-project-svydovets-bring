package com.bobocode.svydovets.context;

import java.util.Map;

import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.factory.BeanFactoryImpl;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;
import org.apache.commons.lang3.StringUtils;

public class AnnotationConfigurationApplicationContext implements ApplicationContext {

    private final BeanFactory beanFactory;
    private final ComponentBeanScanner scanner;

    public AnnotationConfigurationApplicationContext(String packageName) {
        this.beanFactory = new BeanFactoryImpl();
        this.scanner = new ComponentBeanScanner(this);
        this.scan(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        return null;
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanDefinitionException {
        this.beanFactory.createBean(name);
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

    public void scan(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("Package is empty! Please specify the package name.");
        }
        this.beanFactory.setNameToBeanDefinitionMap(this.scanner.scan(packageName));
    }
}
