package com.bobocode.svydovets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bobocode.svydovets.beans.definition.BeanDefinition;

/**
 * This annotation is a marker for the Bring that method marked with this annotation returns Bean.
 * The Bring takes care for all lifecycle and injections of the object marked as {@link Bean}.
 * The annotation is applicable only to methods in classes that are marked as {@link Configuration}.
 * You can pass bean name, if you didn't pass the name then as name will be taken {@link Class#getSimpleName()}
 * with the first latter in lowerCase.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

    /**
     * Specify the name of {@link BeanDefinition} associated with annotated class.
     *
     * @return the suggested component name (if not empty)
     */
    String value() default "";
}
