package com.bobocode.svydovets.context;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.factory.DefaultListableBeanFactory;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContext implements ApplicationContext {
    private Map<String, Object> nameToBean;
    private final BeanFactory beanFactory;
    private final ComponentBeanScanner componentScanner;

    public AnnotationConfigurationApplicationContext(String packageName) {
        this.beanFactory = new DefaultListableBeanFactory();
        this.componentScanner = new ComponentBeanScanner();
        this.scan(packageName);
    }

    public AnnotationConfigurationApplicationContext(BeanFactory beanFactory,
                                                     ComponentBeanScanner componentScanner,
                                                     String packageName) {
        this.beanFactory = beanFactory;
        this.componentScanner = componentScanner;
        this.scan(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        var beans = nameToBean.values().stream()
                .filter(beanType::isInstance)
                .toList();
        if (beans.size() > 1) {
            throw new NoUniqueBeanDefinitionException();
        }
        if (beans.isEmpty()) {
            throw new NoSuchBeanDefinitionException();
        }

        return beanType.cast(beans.get(0));
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanDefinitionException {
        return nameToBean.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanDefinitionException {
        var bean = nameToBean.get(name);
        if (bean == null || !beanType.isInstance(beanType)) {
            throw new NoSuchBeanDefinitionException();
        }
        return beanType.cast(bean);
    }

    @Override
    public Map<String, Object> getBeans(Class<?> beanType) throws NoSuchBeanDefinitionException {
        return  nameToBean.entrySet().stream()
                .filter(entry -> beanType.isInstance(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }

    public void scan(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("Package is empty! Please specify the package name.");
        }
        var componentNameToBeanDefinition = componentScanner.scan(packageName);
        nameToBean = beanFactory.createBeans(componentNameToBeanDefinition);
    }

}
