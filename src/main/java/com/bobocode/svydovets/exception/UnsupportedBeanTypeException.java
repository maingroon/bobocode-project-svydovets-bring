package com.bobocode.svydovets.exception;

/**
 * Exception thrown by BeanScanner in case of declaring void method as a @Bean in @Configuration class.
 */
public class UnsupportedBeanTypeException extends RuntimeException {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public UnsupportedBeanTypeException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public UnsupportedBeanTypeException(String message) {
        super(message);
    }
}
