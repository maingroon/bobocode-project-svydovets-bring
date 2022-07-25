package com.bobocode.svydovets.beans.factory.util;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Logger;

public class BeanFactoryUtil {

    private final static Logger LOG = Logger.getLogger(BeanFactoryUtil.class.getName());
    /**
     * This method return the beanDefiniton from the definitions map, by the name of the bean
     * if name not set directly, it will serach defitintion by the type;
     *
     * @param definitions bean definitionMap, key - bean name or type, value - BeanDefinition;
     * @param typeName    - the name of the type;
     * @return
     */
    public static BeanDefinition getDefinitionByName(Map<String, BeanDefinition> definitions, String typeName) {
        BeanDefinition beanDefinition;
        if (definitions.containsKey(typeName)) {
            beanDefinition = definitions.get(typeName);
        } else {
            beanDefinition = definitions.values()
                    .stream()
                    .filter(definition -> typeName.equals(definition.getBeanClass().getName()))
                    .findFirst().orElseThrow(() -> {
                        throw new NoSuchBeanDefinitionException();
                    });
        }
        return beanDefinition;
    }


    /**
     * This method instantiate a new Bean object using the beanDefinition,
     * currently by no args constructor;
     *
     * @param beanDefinition BeanDefinition describing a bean the will be created;
     * @param <T>            type of the Bean to be returned
     * @return
     */
    public static <T> T newInstance(BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            // TODO add creation strategy to be able to create bean through constructors or fabric.
            // also add logic related constructor args injection
            Constructor<?> beanConstructor = beanDefinition.getBeanClass().getConstructor();
            bean = beanConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            LOG.info("BeanFactoryUtil.newInstance() throw Exception, beanDefinition: " + beanDefinition.getName()
                    + " exception: " + exception.getMessage());
        }
        return (T) bean;
    }
}
