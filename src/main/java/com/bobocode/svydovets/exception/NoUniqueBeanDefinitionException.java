package com.bobocode.svydovets.exception;

public class NoUniqueBeanDefinitionException extends RuntimeException {

    public NoUniqueBeanDefinitionException() {
    }

    public NoUniqueBeanDefinitionException(String message) {
        super(message);
    }
}
