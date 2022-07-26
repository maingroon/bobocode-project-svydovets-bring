package com.bobocode.svydovets.beans.factory;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory implements BeanFactory {


    /** Create a new map of bean instances, from bean definitions.
     * @param definitionMap bean definition map, key - the name of the bean, the value - bean definition object
     * @return bean map, where key - bean name, and value - new instance of the bean
     */
    @Override
    public Map<String, Object> createBeans(Map<String, BeanDefinition> definitionMap) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Object> beanMap = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : definitionMap.entrySet()) {
            Constructor<?> beanConstructor = entry.getValue().getBeanClass().getConstructor();
            beanMap.put(entry.getKey(), beanConstructor.newInstance());
        }
        return beanMap;
    }
}
