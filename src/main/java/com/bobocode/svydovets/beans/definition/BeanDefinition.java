package com.bobocode.svydovets.beans.definition;

import java.lang.reflect.Method;

/**
 * A BeanDefinition describes a bean instance, which has property values, constructor argument values
 * for further bean creation.
 */
public class BeanDefinition {

    private String name;
    private Class<?> beanClass;

    /**
     * Factory method for creation bean instance.
     */
    private Method factoryMethod;

    /**
     * Contains bean names which this bean depends on.
     */
    private String[] dependsOn;

    public BeanDefinition(String name, Class<?> beanClass) {
        this.name = name;
        this.beanClass = beanClass;
    }

    public BeanDefinition(String name, Class<?> beanClass, Method factoryMethod) {
        this.name = name;
        this.beanClass = beanClass;
        this.factoryMethod = factoryMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    public String[] getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }
}
