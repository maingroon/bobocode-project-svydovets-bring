package com.bobocode.svydovets.beans.exception;

/**
 * Exception thrown when a bean can't be instantiated
 */
public class BeanInstantiationException extends RuntimeException {

    /**
     * Create a new exception {@code BeanInstantiationException}
     * @param message the detail message
     */
    public BeanInstantiationException(String message) {
        super(message);
    }

    /**
     * Create a new exception {@code BeanInstantiationException}
     * @param message the detail message
     * @param cause the root cause
     */
    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
