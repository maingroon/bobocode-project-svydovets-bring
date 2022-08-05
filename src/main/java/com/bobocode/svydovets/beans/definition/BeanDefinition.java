package com.bobocode.svydovets.beans.definition;

import java.lang.reflect.Method;

import com.bobocode.svydovets.annotation.Component;
import com.bobocode.svydovets.annotation.Configuration;
import com.bobocode.svydovets.annotation.PostConstruct;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A BeanDefinition describes a bean instance, which has property values, constructor argument values
 * for further bean creation.
 */
@Builder
@Getter
public class BeanDefinition {

    /**
     * Name of the bean. It can be used to get it from {@link com.bobocode.svydovets.context.ApplicationContext}.
     */
    private String name;
    /**
     * Class of the bean which will be created from this definition.
     */
    private Class<?> beanClass;

    /**
     * Factory method for creation bean instance.
     */
    private Method factoryMethod;

    /**
     * Method marked as {@link PostConstruct} in {@link Component} or {@link Configuration} class.
     */
    private Method postConstructMethod;

    /**
     * Class of the configuration bean.
     */
    private Class<?> configurationClass;

    /**
     * Contains bean names which this bean depends on.
     */
    @Setter
    private String[] dependsOn;
}
