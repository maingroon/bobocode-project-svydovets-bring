package com.bobocode.svydovets.exception;

/**
 * Exception thrown when a bean can't be injected
 */
public class BeanInjectionException extends RuntimeException {

    /**
     * Create a new exception {@code BeanInjectionException}
     * @param message the detail message
     * @param cause the root cause
     */
    public BeanInjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
