package com.bobocode.svydovets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

/**
 * This is the marker for the Bring that in class that marked with this annotation some bean configurations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    /**
     * Specify the name of {@link BeanDefinition} associated with annotated class.
     *
     * @return the suggested configuration name (if not empty)
     */
    String value() default "";
}
