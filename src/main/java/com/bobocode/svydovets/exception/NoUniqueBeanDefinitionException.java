package com.bobocode.svydovets.exception;

/**
 * Exception thrown by BeanScanner in case of multiple beans declared with the same name.
 */
public class NoUniqueBeanDefinitionException extends RuntimeException {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public NoUniqueBeanDefinitionException() {
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public NoUniqueBeanDefinitionException(String message) {
        super(message);
    }
}
