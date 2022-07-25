package com.bobocode.svydovets.beans.definition;

import java.lang.reflect.Method;

/**
 * Extends BeanDefinition and contain information that will be further used to create beans instances that
 * were declared in @Configuration classes.
 */
public class ConfigurationBeanDefinition extends BeanDefinition {

    private Method beanMethod;

    public ConfigurationBeanDefinition(String name, Class<?> beanClass, Method beanMethod) {
        super(name, beanClass);
        this.beanMethod = beanMethod;
    }

    /**
     * Returns a Method instance that was annotated with @Bean annotation in @Configuration class.
     *
     * @return method instance
     */
    public Method getBeanMethod() {
        return beanMethod;
    }

    public void setBeanMethod(Method method) {
        this.beanMethod = method;
    }

}
