package com.bobocode.svydovets.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.Inject;
import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.exception.BeanInstantiationException;

public class DefaultListableBeanFactory implements BeanFactory {

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
          .collect(Collectors.toMap(BeanDefinition::getName, DefaultListableBeanFactory::createComponentBean));

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


//    private Object createBeanInstance(BeanDefinition beanDefinition, Map<String, Object> componentBeans,
//                                      Map<String, BeanDefinition> definitionMap) {
//        //instance was already created
//        if (componentBeans.get(beanDefinition.getName()) != null) {
//            var beanInstance = componentBeans.get(beanDefinition.getName());
//        }
//        //bean has no dependencies
//        if (beanDefinition.getDependsOn() == null) {
//            if (beanDefinition.getFactoryMethod() != null) {
//                // bean declared in configuration file
//                try {
//                    var configurationClassInstance = getConfigurationClassInstance(beanDefinition, componentBeans);
//                    var beanInstance = beanDefinition.getFactoryMethod().invoke(configurationClassInstance);
//                } catch (IllegalAccessException | InvocationTargetException ex) {
//                    throw new BeanInstantiationException("Could not instantiate a bean.", ex);
//                }
//            } else {
//                // bean declared as a component
//                try {
//                    var beanInstance = beanDefinition.getBeanClass().getConstructor().newInstance();
//                } catch (InvocationTargetException | InstantiationException
//                         | IllegalAccessException | NoSuchMethodException exception) {
//                    throw new BeanInstantiationException(exception.getMessage());
//                }
//            }
//        }
//        // bean has other dependencies
//        else {
//            for (String dependency : beanDefinition.getDependsOn()) {
//                var dependencyBeanDefinition = definitionMap.get(dependency);
//                var beanInstance = createBeanInstance(dependencyBeanDefinition, componentBeans, definitionMap);
//            }
//        }
//        return null;
//    }

    private static Object getConfigurationClassInstance(BeanDefinition beanDefinition,
                                                        Map<String, Object> componentBeans) {
        var declaredClass = beanDefinition.getBeanClass();
        var declaredClassConfigValue = declaredClass.getAnnotation(Configuration.class).value().trim();
        var componentBeanName =
          declaredClassConfigValue.isEmpty() ? StringUtils.uncapitalize(declaredClass.getName()) :
            declaredClassConfigValue;
        return componentBeans.get(componentBeanName);
    }

    private static Pair<String, Object> createConfigurationDeclaredBean(BeanDefinition beanDefinition,
                                                                        Map<String, Object> componentBeans) {
        var configurationClassInstance = getConfigurationClassInstance(beanDefinition, componentBeans);
        try {

            var beanInstance = beanDefinition.getFactoryMethod().invoke(configurationClassInstance,
              new Object[beanDefinition.getFactoryMethod().getParameters().length]);
//              (Object[]) beanDefinition.getFactoryMethod().getParameters());
            return Pair.of(beanDefinition.getName(), beanInstance);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new BeanInstantiationException(
              String.format("Could not instantiate a bean: %s", beanDefinition.getName()), ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException(
              String.format("Could not instantiate a bean: %s. Default constructor should be created.",
                beanDefinition.getName()), ex);
        }
    }

//    private Object getBeanInstanceOrCreateNew(Map<String, Object> createdBeans, String beanName,
//                                              Map<String, BeanDefinition> definitionMap) {
//        if (createdBeans.get(beanName) != null) {
//            return createdBeans.get(beanName);
//        }
//        var beanDef = definitionMap.get(beanName);
//
//        if (beanDef.getFactoryMethod() != null) {
//            if (beanDef.getDependsOn().length == 0) {
//
//            }
//        } else {
//
//        }
//    }

//    private static List<Object> getMethodParameterObjects(Map<String, Object> componentBeans, String[] dependOnBeans) {
//        for (String db : dependOnBeans) {
//            var bean = componentBeans.get(db);
//        }
//    }


//    private void test(Map<String, Object> componentBeans, String dependOnBean) {
//        if (componentBeans.get(dependOnBean) != null) {
//            return;
//        }
//        test(componentBeans, )
//    }

    private static Object createComponentBean(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.getBeanClass().getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException exception) {
            throw new BeanInstantiationException(exception.getMessage());
        }
    }

//    private void injectAll(Map<String, Object> beanMap) {
//        injectComponentBeans(beanMap);
//        injectConfigurationBeans();
////        beanMap.values().forEach(bean ->
////          Arrays.stream(bean.getClass().getDeclaredFields())
////            .filter(field -> field.isAnnotationPresent(Inject.class))
////            .forEach(field -> inject(bean, field, beanMap)));
//    }

    private void inject(Object bean, Field field, Map<String, Object> beanMap) {
        Inject fieldAnnotation = field.getAnnotation(Inject.class);

        if (Objects.nonNull(fieldAnnotation) && StringUtils.isNotEmpty(fieldAnnotation.value())) {
            injectToFiled(bean, field, beanMap.get(fieldAnnotation.value()));
        } else {
            injectToFiled(bean, field, beanMap.get(field.getType().getName()));
        }
    }

    private void injectComponentBeans(Map<String, Object> beanMap) {
        beanMap.values().forEach(bean ->
          Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> inject(bean, field, beanMap)));
    }

    private void injectConfigurationBeans(Map<String, BeanDefinition> definitionMap, Map<String, Object> beanMap) {
        definitionMap.values().stream()
          .filter(beanDefinition -> beanDefinition.getDependsOn() != null)
          .forEach(beanDefinition -> inject(beanDefinition, beanMap));
    }

    private void inject(BeanDefinition beanDefinition, Map<String, Object> beanMap) {
        var bean = beanMap.get(beanDefinition.getName());
        var beanClass = bean.getClass();

        var dependencyMap = Arrays.stream(beanDefinition.getDependsOn())
          .collect(Collectors.toMap(Function.identity(), Function.identity()));

        Arrays.stream(beanClass.getDeclaredFields())
          .filter(field -> dependencyMap.get(field.getType().getName()) != null)
          .forEach(field -> inject(bean, field, beanMap));
    }

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
