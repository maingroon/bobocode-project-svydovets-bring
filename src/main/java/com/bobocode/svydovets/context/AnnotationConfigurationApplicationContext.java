package com.bobocode.svydovets.context;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.factory.DefaultListableBeanFactory;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

public class AnnotationConfigurationApplicationContext implements ApplicationContext {
    private Map<String, Object> beanContainer;
    private final BeanFactory beanFactory;
    private final ComponentBeanScanner componentScanner;

    public AnnotationConfigurationApplicationContext(String packageName) {
        this.beanFactory = new DefaultListableBeanFactory();
        this.componentScanner = new ComponentBeanScanner();
        init(packageName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        Objects.requireNonNull(beanType, "The beanType cannot be null!");

        var beans = beanContainer.values().stream()
          .filter(beanType::isInstance)
          .toList();

        if (beans.size() > 1) {
            throw new NoUniqueBeanDefinitionException(String.format("The Bring contains %s beans of the %s. Please, "
              + "specify a beanName", beans.size(), beanType.getSimpleName()));
        }
        if (beans.isEmpty()) {
            throw new NoSuchBeanDefinitionException(String.format("Bean with type %s is not found",
              beanType.getSimpleName()));
        }

        return beanType.cast(beans.get(0));
    }

    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        Objects.requireNonNull(beanName, "The beanName cannot be null!");

        if (StringUtils.isEmpty(beanName)) {
            throw new IllegalArgumentException("Bean name is empty! Please specify the bean name.");
        }

        var bean = beanContainer.get(beanName);
        if (Objects.isNull(bean)) {
            throw new NoSuchBeanDefinitionException(String.format("Bean with name %s is not found", beanName));
        }

        return bean;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType) throws NoSuchBeanDefinitionException {
        Objects.requireNonNull(beanName, "The beanName cannot be null!");
        Objects.requireNonNull(beanType, "The beanType cannot be null!");
        if (StringUtils.isEmpty(beanName)) {
            throw new IllegalArgumentException("Bean name is empty! Please specify the bean name.");
        }

        var bean = beanContainer.get(beanName);
        if (bean == null) {
            throw new NoSuchBeanDefinitionException(String.format("Bean with name %s is not found", beanName));
        } else if (!ClassUtils.isAssignable(bean.getClass(), beanType)) {
            throw new NoSuchBeanDefinitionException(
              String.format("Bean with name %s is not an instance of the %s. %s is the instance of the %s",
                beanName, beanType.getSimpleName(), beanName, bean.getClass().getSimpleName()));
        }

        return beanType.cast(bean);
    }

    @Override
    // TODO: should we return empty Map in case if we not find instances of beanType or throwException?
    public Map<String, Object> getBeans(Class<?> beanType) throws NoSuchBeanDefinitionException {
        return beanContainer.entrySet().stream()
          .filter(entry -> beanType.isInstance(entry.getValue()))
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }

    private void init(String packageName) {
        scanAndCreate(packageName);
        injectAll();
    }

    private void scanAndCreate(String packageName) {
        Objects.requireNonNull(packageName, "Package name cannot be null! Please specify the package name.");
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("Package name is empty! Please specify the package name.");
        }

        var componentNameToBeanDefinition = componentScanner.scan(packageName);
        beanContainer = beanFactory.createBeans(componentNameToBeanDefinition);
    }

    private void injectAll() {
        beanContainer.values().forEach(bean ->
          Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> inject(bean, field)));
    }

    private void inject(Object bean, Field field) {
        Inject fieldAnnotation = field.getAnnotation(Inject.class);

        if (StringUtils.isNotEmpty(fieldAnnotation.value())) {
            injectToFiled(bean, field, getBean(fieldAnnotation.value()));
        } else {
            injectToFiled(bean, field, getBean(field.getType()));
        }
    }

    private void injectToFiled(Object bean, Field field, Object injectBean) {
        try {
            field.setAccessible(true);
            field.set(bean, injectBean);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(String.format("Unable to inject %s into %s. Fall with exception: %s",
              injectBean.getClass().getSimpleName(), bean.getClass().getSimpleName(), exception));
        }
    }

}
