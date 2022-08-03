package com.bobocode.svydovets.exception;

public class BeanPostprocessorInstantiationException extends RuntimeException {
    public BeanPostprocessorInstantiationException(String message) {
        super(message);
    }

    public BeanPostprocessorInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
