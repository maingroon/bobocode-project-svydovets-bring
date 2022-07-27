package com.bobocode.svydovets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is a marker for the Bring that Bring have to inject into marked field a bean of this type.
 * This annotation is applicable to field.
 * You can pass bean name to qualify what kind of bean should be injected, if you didn't pass, then the Bring will
 * search by type.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

    String value() default "";
}
