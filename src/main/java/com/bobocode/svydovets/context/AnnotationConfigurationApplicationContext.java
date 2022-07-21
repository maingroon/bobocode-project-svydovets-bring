package com.bobocode.svydovets.context;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.factory.BeanFactoryImpl;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.ConfigurationBeanScanner;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContext implements ApplicationContext {

    private final BeanFactory beanFactory;
    private final ConfigurationBeanScanner configScanner;
    private final ComponentBeanScanner componentScanner;

    public AnnotationConfigurationApplicationContext(String packageName) {
        this.beanFactory = new BeanFactoryImpl();
        this.configScanner = new ConfigurationBeanScanner();
        this.componentScanner = new ComponentBeanScanner();
        this.scan(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        return this.beanFactory.createBean(beanType);
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanDefinitionException {
        return this.beanFactory.createBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanDefinitionException {
        return this.beanFactory.createBean(name, beanType);
    }

    @Override
    public Map<String, Object> getBeans(Class<?> beanType) throws NoSuchBeanDefinitionException {
        return this.beanFactory.createBeans(beanType);
    }

    public void scan(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("Package is empty! Please specify the package name.");
        }
        Map<String, BeanDefinition> configNameToBeanDefinition = this.configScanner.scan(packageName);
        Map<String, BeanDefinition> componentNameToBeanDefinition = this.componentScanner.scan(packageName);
        addNameToBeanDefinitionMap(configNameToBeanDefinition);
        addNameToBeanDefinitionMap(componentNameToBeanDefinition);
    }

    private void addNameToBeanDefinitionMap(Map<String, BeanDefinition> nameToBeanDefinition) {
        if (nameToBeanDefinition != null && !nameToBeanDefinition.isEmpty()) {
            this.beanFactory.setNameToBeanDefinitionMap(nameToBeanDefinition);
        }
    }
}
