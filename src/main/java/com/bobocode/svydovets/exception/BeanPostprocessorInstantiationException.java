package com.bobocode.svydovets.exception;

/**
 * Exception thrown when a BeanPostprocessor can't be instantiated
 */
public class BeanPostprocessorInstantiationException extends RuntimeException {

    /**
     * Create a new exception {@code BeanPostprocessorInstantiationException}
     * @param message
     */
    public BeanPostprocessorInstantiationException(String message) {
        super(message);
    }

    /**
     * Create a new exception {@code BeanPostprocessorInstantiationException}
     * @param message the detail message
     * @param cause the root cause
     */
    public BeanPostprocessorInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
