package com.bobocode.svydovets.annotation;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    /**
     * Specify the name of {@link BeanDefinition} associated with annotated class.
     *
     * @return the suggested component name (if not empty)
     */
    String value() default "";
}
