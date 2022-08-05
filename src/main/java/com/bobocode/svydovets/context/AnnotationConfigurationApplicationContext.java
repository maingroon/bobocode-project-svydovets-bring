package com.bobocode.svydovets.context;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.bobocode.svydovets.beans.factory.BeanFactory;
import com.bobocode.svydovets.beans.factory.DefaultListableBeanFactory;
import com.bobocode.svydovets.beans.scanner.BeanPostprocessorScanner;
import com.bobocode.svydovets.beans.scanner.BeanScanner;
import com.bobocode.svydovets.beans.scanner.ComponentBeanScanner;
import com.bobocode.svydovets.beans.scanner.ConfigurationBeanScanner;
import com.bobocode.svydovets.beans.utils.BeanUtils;
import com.bobocode.svydovets.exception.BeansException;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

/**
 * This class is the implementation of the {@link ApplicationContext} that provides realisation for annotation
 * configuration.
 * The {@link AnnotationConfigurationApplicationContext} contains Bring-Bean-Container {@link Map} and implementations
 * of the methods of the {@link ApplicationContext} for provisioning Beans by beanName and beanType.
 * This implementation takes control of lifecycle of Beans (implement Inversion of Control design principle), and
 * implement Dependency Injection design pattern.
 */
public class AnnotationConfigurationApplicationContext implements ApplicationContext {
    private Map<String, Object> beanContainer;
    private final BeanFactory beanFactory;
    private final BeanScanner[] scanners;

    public AnnotationConfigurationApplicationContext(String packageName) {
        String validatedPackage = BeanUtils.validatePackageName(packageName);
        BeanPostprocessorScanner postprocessorScanner = new BeanPostprocessorScanner();
        this.beanFactory = new DefaultListableBeanFactory(postprocessorScanner.scan(validatedPackage));
        this.scanners = new BeanScanner[]{new ComponentBeanScanner(), new ConfigurationBeanScanner()};
        scanAndCreate(validatedPackage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException {
        checkIsNotNullBeanType(beanType);

        var beans = beanContainer.values().stream()
          .filter(beanType::isInstance)
          .toList();

        if (beans.size() > 1) {
            throw new NoUniqueBeanDefinitionException(String.format("The Bring contains [%s] beans of the '%s'. "
              + "Please, specify a beanName", beans.size(), beanType.getSimpleName()));
        }
        if (beans.isEmpty()) {
            throw new NoSuchBeanDefinitionException(
                String.format("Bean with beanType '%s' is not found! Please check your package name and "
                        + " instances of @Component or @Configuration with @Bean objects under your package",
                beanType.getSimpleName()));
        }

        return beanType.cast(beans.get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        checkIsNotNullAndNotEmptyBeanName(beanName);

        var bean = beanContainer.get(beanName);
        if (isBeanNotNull(bean, beanName)) {
            return bean;
        }

        throw new BeansException(
          String.format(
            "Unexpected BeansException. Something was going wrong in attempt to get bean by beanName '%s'", beanName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBean(String beanName, Class<T> beanType) throws NoSuchBeanDefinitionException {
        checkIsNotNullAndNotEmptyBeanName(beanName);
        checkIsNotNullBeanType(beanType);

        var bean = beanContainer.get(beanName);
        if (isBeanNotNull(bean, beanName) && !ClassUtils.isAssignable(bean.getClass(), beanType)) {
            throw new NoSuchBeanDefinitionException(
              String.format("Bean with beanName '%s' is not an instance of the %s. '%s' is the instance of the %s",
                beanName, beanType.getSimpleName(), beanName, bean.getClass().getSimpleName()));
        }

        return beanType.cast(bean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getBeans(Class<?> beanType) {
        checkIsNotNullBeanType(beanType);

        return beanContainer.entrySet().stream()
          .filter(entry -> ClassUtils.isAssignable(entry.getValue().getClass(), beanType))
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }

    private void scanAndCreate(String packageName) {
        var componentNameToBeanDefinition = Arrays.stream(scanners)
                .flatMap(s -> s.scan(packageName).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Arrays.stream(scanners).forEach(s -> s.fillDependsOn(componentNameToBeanDefinition));
        beanContainer = beanFactory.createBeans(componentNameToBeanDefinition);
    }

    private void checkIsNotNullAndNotEmptyBeanName(String beanName) {
        Objects.requireNonNull(beanName, "The beanName cannot be null! Please specify beanName.");
        if (StringUtils.isEmpty(beanName)) {
            throw new IllegalArgumentException("The beanName is empty! Please specify beanName.");
        }
    }

    private void checkIsNotNullBeanType(Class<?> beanType) {
        Objects.requireNonNull(beanType, "The beanType cannot be null! Please specify beanType.");
    }

    private boolean isBeanNotNull(Object bean, String beanName) {
        if (Objects.isNull(bean)) {
            throw new NoSuchBeanDefinitionException(String.format("Bean with name '%s' is not found", beanName));
        }
        return true;
    }

}
