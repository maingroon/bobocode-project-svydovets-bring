package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.bpp.BeanPostProcessor;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.exception.BeanInstantiationException;
import com.bobocode.svydovets.exception.BeanInjectionException;
import com.bobocode.svydovets.exception.NoSuchBeanException;
import com.bobocode.svydovets.exception.NoUniqueBeanException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Default BeanFactory implementation.
 */
@Log4j2
@RequiredArgsConstructor
public class DefaultListableBeanFactory implements BeanFactory {

    @Getter
    private final Set<BeanPostProcessor> beanPostProcessors;

    /**
     * Create a new map of bean instances, from bean definitions.
     *
     * @param definitionMap bean definition map, key - the name of the bean, the value - bean definition object
     * @return bean map, where key - bean name, and value - new instance of the bean
     */
    @Override
    public Map<String, Object> createBeans(Map<String, BeanDefinition> definitionMap) {
        var componentBeans = definitionMap.values().stream()
          .filter(bd -> Objects.isNull(bd.getFactoryMethod()))
          .collect(Collectors.toMap(BeanDefinition::getName, this::createComponentBean));

        var configurationDeclaredBeans = definitionMap.values().stream()
          .filter(bd -> Objects.nonNull(bd.getFactoryMethod()))
          .map(confDeclaredBeanDefinitionEntry -> createConfigurationDeclaredBean(confDeclaredBeanDefinitionEntry,
            componentBeans))
          .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

        Map<String, Object> beanMap = Stream.of(componentBeans, configurationDeclaredBeans)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        injectComponentBeans(beanMap);
        injectConfigurationBeans(definitionMap, beanMap);

        return beanMap;
    }

    /**
     * Creates a Pair consisting of Bean definition name and bean object.
     * @param beanDefinition the bean definition
     * @param componentBeans map, where key - bean name, and value - bean object
     * @return Pair left object - bean name, right object - bean object
     * @throws BeanInstantiationException if the underlying method throws an exception
     */
    private Pair<String, Object> createConfigurationDeclaredBean(BeanDefinition beanDefinition,
                                                                 Map<String, Object> componentBeans) {
        var declaredClass = beanDefinition.getConfigurationClass();
        var declaredClassConfigValue = declaredClass.getAnnotation(Configuration.class).value().trim();
        var componentBeanName =
          declaredClassConfigValue.isEmpty() ? StringUtils.uncapitalize(declaredClass.getName()) :
            declaredClassConfigValue;
        var declaredClassInstance = componentBeans.get(componentBeanName);
        try {
            var beanInstance = beanDefinition.getFactoryMethod().invoke(declaredClassInstance,
              new Object[beanDefinition.getFactoryMethod().getParameters().length]);
            var beanInstanceProcessedBeforeInitialization =
              applyPostprocessorsBeforeInitialization(beanInstance, componentBeanName);
            var beanInstanceProcessedAfterInitialization =
              applyPostprocessorsAfterInitialization(beanInstanceProcessedBeforeInitialization, componentBeanName);
            return Pair.of(beanDefinition.getName(), beanInstanceProcessedAfterInitialization);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new BeanInstantiationException("Could not instantiate a bean.", ex);
        }
    }

    /**
     * Creates a bean from bean definition.
     * @param beanDefinition the bean definition
     * @return bean object
     * @throws BeanInstantiationException if the underlying methods throws an exception
     */
    private Object createComponentBean(BeanDefinition beanDefinition) {
        try {
            Object originalBean = beanDefinition.getBeanClass().getConstructor().newInstance();
            Object modifiedBean =
              applyPostprocessorsBeforeInitialization(originalBean, beanDefinition.getName());
            if (beanDefinition.getPostConstructMethod() != null) {
                beanDefinition.getPostConstructMethod().invoke(modifiedBean);
            }
            modifiedBean = applyPostprocessorsAfterInitialization(modifiedBean, beanDefinition.getName());
            return modifiedBean;
        } catch (InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException exception) {
            throw new BeanInstantiationException(
              String.format("Could not instantiate a bean: %s. Default constructor should be created.",
                beanDefinition.getName()), exception);
        }
    }

    /**
     * Applies postprocessor to bean.
     * @param bean bean object to modify
     * @param beanName string name of the bean
     * @return modified bean object
     */
    private Object applyPostprocessorsBeforeInitialization(Object bean, String beanName) {
        var result = bean;
        for (var postprocessor : beanPostProcessors) {
            result = postprocessor.postProcessBeforeInitialization(bean, beanName);
            if (Objects.isNull(result)) {
                log.info("Postprocessor {} returns null, all subsequent postprocessors will be skipped",
                  postprocessor.getClass().getSimpleName());
                break;
            }
        }
        return result;
    }

    private Object applyPostprocessorsAfterInitialization(Object bean, String beanName) {
        var result = bean;
        for (var postprocessor : beanPostProcessors) {
            result = postprocessor.postProcessAfterInitialization(bean, beanName);
            if (result == null) {
                log.info("Postprocessor {} returns null, all subsequent postprocessors will be skipped",
                  postprocessor.getClass().getSimpleName());
                break;
            }
        }
        return result;
    }

    private void injectComponentBeans(Map<String, Object> beanMap) {
        beanMap.values().forEach(bean ->
          Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> inject(bean, field, beanMap)));
    }

    private void injectConfigurationBeans(Map<String, BeanDefinition> definitionMap, Map<String, Object> beanMap) {
        definitionMap.values().stream()
          .filter(beanDefinition -> Objects.nonNull(beanDefinition.getDependsOn()))
          .forEach(beanDefinition -> inject(beanDefinition, beanMap));
    }

    private void inject(Object bean, Field field, Map<String, Object> beanMap) {
        Inject fieldAnnotation = field.getAnnotation(Inject.class);

        if (Objects.nonNull(fieldAnnotation) && StringUtils.isNotEmpty(fieldAnnotation.value())) {
            injectToFiled(bean, field, beanMap.get(fieldAnnotation.value()));
        } else {
            try {
                var injectBean = findBeanInstance(beanMap, field);
                injectToFiled(bean, field, injectBean);
            } catch (NoSuchBeanException | NoUniqueBeanException ex) {
                throw new BeanInjectionException(
                  String.format("Error injecting the field %s into bean %s", field.getName(),
                    bean.getClass().getName()), ex);
            }
        }
    }

    private void inject(BeanDefinition beanDefinition, Map<String, Object> beanMap) {
        var bean = beanMap.get(beanDefinition.getName());
        var beanClass = bean.getClass();

        var dependencyMap = Arrays.stream(beanDefinition.getDependsOn())
          .collect(Collectors.toMap(Function.identity(), beanMap::get));

        Arrays.stream(beanClass.getDeclaredFields())
          .filter(field -> Objects.nonNull(dependencyMap.get(field.getType().getName()))
            || dependencyMap.values().stream()
            .anyMatch(beanInstance -> beanInstance.getClass().isAssignableFrom(field.getType())))
          .forEach(field -> inject(bean, field, beanMap));
    }

    private Object findBeanInstance(Map<String, Object> beanMap, Field field) {
        var fieldType = field.getType().getName();
        var beanInstance = beanMap.get(fieldType);
        if (Objects.isNull(beanInstance)) {
            var result = beanMap.values().stream()
              .filter(beanInst -> beanInst.getClass().getName().equals(fieldType)).toList();
            if (result.isEmpty()) {
                throw new NoSuchBeanException(String.format("Could not find a bean with the type: %s", fieldType));
            }
            if (result.size() > 1) {
                throw new NoUniqueBeanException(String.format("Multiple beans of type: %s found. "
                  + "Current version doesn't support any constructor bean qualifiers so it's possible only to "
                  + "pass a single type created bean as a constructor argument", fieldType));
            }
            return result.get(0);
        }
        return beanInstance;
    }

    /**
     * Injects a bean object into a field of another bean.
     * @param bean a bean object where injectBean will be injected
     * @param field a field to which injectBean will be injected
     * @param injectBean bean to be injected
     */
    private void injectToFiled(Object bean, Field field, Object injectBean) {
        try {
            field.setAccessible(true);
            field.set(bean, injectBean);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(String.format("Unable to inject '%s' into '%s'. Fall with exception: [%s]",
              injectBean.getClass().getSimpleName(), bean.getClass().getSimpleName(), exception));
        }
    }
}
