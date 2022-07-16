package com.bobocode.svydovets.exception;

public class NoSuchBeanConfigurationException extends RuntimeException {

    public NoSuchBeanConfigurationException() {
    }

    public NoSuchBeanConfigurationException(String message) {
        super(message);
    }
}
